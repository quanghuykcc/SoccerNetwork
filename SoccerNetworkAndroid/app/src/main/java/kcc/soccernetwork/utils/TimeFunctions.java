package kcc.soccernetwork.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimeFunctions {
	private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss"; 
	private SimpleDateFormat dateFormat;
	
	public TimeFunctions() {
		dateFormat = new SimpleDateFormat(FORMAT_PATTERN);
	}
	
	public Date formatDate(String dateString) {
		Date result = null;
		try {
			result = dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String toDateString(Calendar calendar) {
		return dateFormat.format(calendar.getTime());
	}
	
	public String calculteDateDif(Date date) {
		Date now = new Date();
		long milliseconds = now.getTime() - date.getTime();
		long minutes = milliseconds / 1000 / 60;
		if (minutes == 0) {
			return "Ngay bây giờ";
		}
		else if (minutes < 60) {
			return minutes + " phút trước";
		}
		else {
			long hours = minutes / 60;
			if (hours < 24) {
				return hours + " giờ trước";
			}
			else {
				int days = (int) (hours / 24);
				if (days < 30) {
					return days + " ngày trước";
				}
				else {
					int months = days / 30;
					if (months < 12) {
						return months + " tháng trước";
					}
					else {
						int years = months / 12;
						return years + " năm trước";
					}
				}
			}
		}
		
	}
}