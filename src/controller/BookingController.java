package controller;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import model.BookingModel;
import model.Trip;

public class BookingController {
	
	DatabaseRetrival dbRetrival = new DatabaseRetrival();
	DatabaseUpdater dbUpdater = new DatabaseUpdater();

	
	public boolean bookTrip(String[] tripInfo) { // tripInfo er listi af tripId, bookerEmail, numPeople, bookerSSN
		
		boolean bookingComplete = false;
		
		BookingModel booking = new BookingModel(-1, Integer.parseInt(tripInfo[0]), tripInfo[1], Integer.parseInt(tripInfo[2]), Integer.parseInt(tripInfo[3]));
				
		bookingComplete = dbUpdater.insertBooking(booking);
				
		if(bookingComplete) {
			sendVerification(Integer.parseInt(tripInfo[1]));
		}
		
		return bookingComplete;	
	}
	
	public void sendVerification(int tripId) {
			
		BookingModel[] tripBookings = dbRetrival.queryTripBooking(tripId);
		
		for(int i=0; i < tripBookings.length; i++) {
			contact(tripBookings[i].getBookerEmail(), tripId);
		}
	}
	
	public void contact(String email, int tripId) {

        final String username = "utrippin7d@gmail.com";
        final String password = "kulpassword";

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
          });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("utrippin7d@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(email));
            
            Trip[] tripInfo = dbRetrival.queryTripInfo(tripId);
            System.out.println(tripInfo.length);
            
            message.setSubject("Booking confirmation");
            message.setText("Dear sir/madam," + "\n" + "\n"
            		+ "We have received your booking." + "\n" + "\n"
            		+ "Here are the information concerning your trip:" + "\n"
            		+ "\tTrip: " + tripInfo[0].getTripName() + "\n"
            		+ "\tFrom: " + tripInfo[0].getDateBegin() + "\n"
            		+ "\tTo: " + tripInfo[0].getDateEnd() + "\n"
            		+ "\tDescription: " + tripInfo[0].getDescription() + "\n"
            		+ "\tLocation: " + tripInfo[0].getLocation() + "\n" + "\n"
            		+ "If you have any questions, please feel free to contact us by email utrippin7d@gmail.com." + "\n\n"
            		+ "Yours sincerely," + "\n"
            		+ "uTrippin"
            		);           

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}