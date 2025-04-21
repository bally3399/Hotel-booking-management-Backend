package topg.bimber_user_service.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import topg.bimber_user_service.dto.requests.BookingRequestDto;
import topg.bimber_user_service.dto.responses.BookingResponseDto;
import topg.bimber_user_service.exceptions.UserNotFoundException;
import topg.bimber_user_service.exceptions.UserNotFoundInDb;
import topg.bimber_user_service.mail.MailService;
import topg.bimber_user_service.models.*;
import topg.bimber_user_service.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final HotelRepository hotelRepository;
    private final MailService mailService;
    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Override
    @Transactional
    public Booking bookRoom(BookingRequestDto request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        if (request.getStartDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
        if (request.getHotelId() == null ) {
            throw new IllegalArgumentException("Hotel name is required");
        }

        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found: " + request.getHotelId()));

        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(()-> new IllegalArgumentException("Room not Found"));
        room.setAvailable(false);
        roomRepository.save(room);

        User user = userRepository.findById(request.getUserId()).orElseThrow(()-> new UserNotFoundException("User Not Found"));

        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        BigDecimal totalPrice = room.getPrice().multiply(BigDecimal.valueOf(days > 0 ? days : 1));

        Booking booking = Booking.builder()
                .user(user)
                .room(room)
                .hotel(hotel)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(BookingStatus.ACTIVE)
                .isPaid(false)
                .totalPrice(totalPrice)
                .build();

        return bookingRepository.save(booking);
    }
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    @Override
    public void updateExpiredBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> expiredBookings = bookingRepository.findExpiredBookings(now);
        for (Booking booking : expiredBookings) {
            booking.setStatus(BookingStatus.EXPIRED);
            Room room = booking.getRoom();
            room.setAvailable(true);
            roomRepository.save(room);
            bookingRepository.save(booking);
        }
    }

    private void validateBookingConditions(User user, Room room, BookingRequestDto bookingRequestDto) {
        LocalDateTime startDate = bookingRequestDto.getStartDate();
        LocalDateTime endDate = bookingRequestDto.getEndDate();

        if (!room.isAvailable()) {
            throw new IllegalStateException("Room is not available");
        }

        if (startDate.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot book a room for past dates");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalStateException("End date cannot be before start date");
        }

//        if (bookingRepository.existsByUserIdAndHotelIdAndStatus(user.getId(), bookingRequestDto.getHotelId(), BookingStatus.CONFIRMED)) {
//            throw new IllegalStateException("You already have an active booking for this hotel");
//        }

//        if (bookingRepository.existsByRoomIdAndDatesOverlap(room.getId(), startDate, endDate)) {
//            throw new IllegalStateException("Room is already booked for the selected dates");
//        }

        if (user.getBalance().compareTo(room.getPrice()) < 0) {
            throw new IllegalStateException("Insufficient balance to book the room");
        }
    }

    private BigDecimal calculateTotalPrice(BigDecimal pricePerNight, LocalDateTime startDate, LocalDateTime endDate) {
        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        return pricePerNight.multiply(BigDecimal.valueOf(numberOfDays));
    }

//    private Booking createBooking(User user, Room room, Hotel hotel, BookingRequestDto bookingRequestDto, BigDecimal totalPrice) {
//        Booking booking = Booking.builder()
//                .user(user)
//                .room(room)
//                .hotel(hotel)
//                .startDate(bookingRequestDto.getStartDate())
//                .endDate(bookingRequestDto.getEndDate())
//                .status(BookingStatus.CONFIRMED)
//                .isPaid(true)
//                .totalPrice(totalPrice)
//                .build();
//        return bookingRepository.save(booking);
//    }

    private void createPayment(User user, BigDecimal amount, Long bookingId) {
        Payment payment = Payment.builder()
                .bookingId(bookingId)
                .amount(amount)
                .success(true)
                .user(user)
                .build();
        paymentRepository.save(payment);
    }

    @Async
    protected void scheduleRoomAvailabilityReset(Room room, LocalDateTime endDate) {
        long delay = ChronoUnit.MILLIS.between(LocalDateTime.now(), endDate.toLocalDate().atStartOfDay());

        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            room.setAvailable(true);
            roomRepository.save(room);
        }, delay, TimeUnit.MILLISECONDS);
    }

    private void sendConfirmationEmail(User user, Room room, Booking booking) {
        Hotel hotel = room.getHotel();
        String emailMessage = String.format(
                """
                        Dear %s,

                        Thank you for booking a room at %s.

                        Your booking details:
                        - Hotel: %s
                        - Room: %s
                        - Start Date: %s
                        - End Date: %s

                        Your booking has been confirmed successfully. We look forward to hosting you!

                        Best regards,
                        %s Team""",
                user.getUsername(),
                hotel.getName(),
                hotel.getName(),
                room.getRoomType(),
                booking.getStartDate(),
                booking.getEndDate(),
                hotel.getName()
        );

        try {
            mailService.sendMail(new NotificationEmail(
                    "Room Successfully Booked",
                    user.getEmail(),
                    emailMessage
            ));
        } catch (Exception e) {
            log.error("Failed to send booking confirmation email to user: {}", user.getEmail(), e);
        }
    }

    private BookingResponseDto buildBookingResponseDto(Booking booking) {
        return new BookingResponseDto(
                booking.getId(),
                booking.getUser().getId(),
                booking.getRoom().getId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getStatus().name(),
                booking.isPaid()
        );
    }

    private <T> T getEntityOrThrow(Supplier<Optional<T>> supplier, String errorMessage) {
        return supplier.get().orElseThrow(() -> new IllegalArgumentException(errorMessage));
    }



    // Cancels an existing booking.
    @Override
    public String cancelBooking(Long bookingId, String userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new UserNotFoundInDb("Booking not found"));

        // Ensure only the user who booked the room can cancel it
        if (!booking.getUser().getId().equals(userId)) {
            throw new IllegalStateException("You can only cancel your own booking.");
        }

        // Ensure only confirmed bookings can be cancelled
        if (!booking.getStatus().equals(BookingStatus.CONFIRMED)) {
            throw new IllegalStateException("Only confirmed bookings can be cancelled.");
        }

        // Restore room availability
        Room room = booking.getRoom();
        room.setAvailable(true);
        roomRepository.save(room);

        // Refund the user
        User user = booking.getUser();
        user.setBalance(user.getBalance().add(room.getPrice()));
        userRepository.save(user);

        // Update booking status to CANCELLED
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        // Send cancellation email to the user
        sendCancellationEmail(user, room, booking);

        return "Booking with ID " + bookingId + " has been cancelled successfully. Refund processed.";
    }


    private void sendCancellationEmail(User user, Room room, Booking booking) {
        Hotel hotel = room.getHotel();
        String emailMessage = String.format(
                """
                        Dear %s,

                        Your booking at %s has been successfully cancelled.

                        Booking details:
                        - Hotel: %s
                        - Room: %s
                        - Start Date: %s
                        - End Date: %s

                        A refund of %s has been credited back to your account.

                        We hope to see you again soon!

                        Best regards,
                        %s Team""",
                user.getUsername(),
                hotel.getName(),
                hotel.getName(),
                room.getRoomType(),
                booking.getStartDate(),
                booking.getEndDate(),
                room.getPrice(),
                hotel.getName()
        );

        try {
            mailService.sendMail(new NotificationEmail(
                    "Booking Cancellation Confirmation",
                    user.getEmail(),
                    emailMessage
            ));
        } catch (Exception e) {
            log.error("Failed to send booking cancellation email to user: {}", user.getEmail(), e);
        }
    }



    // Updates an existing booking.
    @Override
    @CachePut(value = "bookings", key = "#bookingId")
    public BookingResponseDto updateBooking(Long bookingId, BookingRequestDto bookingRequestDto) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new UserNotFoundInDb("Booking not found"));

//        Room room = roomRepository.findById(bookingRequestDto.getRoomId())
//                .orElseThrow(() -> new UserNotFoundInDb("Room not found"));

//        booking.setRoom(room);
        booking.setStartDate(bookingRequestDto.getStartDate());
        booking.setEndDate(bookingRequestDto.getEndDate());

        booking = bookingRepository.save(booking);

        return new BookingResponseDto(
                booking.getId(),
                booking.getUser().getId(),
                booking.getRoom().getId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getStatus().name(),
                booking.isPaid()
        );
    }

    @Override
    public BookingResponseDto findBookingByRoomId(Long roomId) {
        Booking booking = bookingRepository.findByRoomId(roomId).orElseThrow(()-> new IllegalArgumentException("Booking not found"));
        return  new BookingResponseDto(
                booking.getId(),
                booking.getUser().getId(),
                booking.getRoom().getId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getStatus().name(),
                booking.isPaid()
        );
    }

    @Override
    public List<BookingResponseDto> listAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(booking -> new BookingResponseDto(
                        booking.getId(),
                        booking.getUser().getId(),
                        booking.getRoom().getId(),
                        booking.getStartDate(),
                        booking.getEndDate(),
                        booking.getStatus().name(),
                        booking.isPaid()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getUserBookings(String userId) {
        List<Booking> bookings = bookingRepository.findBookingsByUserId(userId);
        return bookings.stream()
                .map(booking -> new BookingResponseDto(
                        booking.getId(),
                        booking.getUser().getId(),
                        booking.getRoom().getId(),
                        booking.getStartDate(),
                        booking.getEndDate(),
                        booking.getStatus().name(),
                        booking.isPaid()
                ))
                .collect(Collectors.toList());
    }
}
