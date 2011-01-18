package za.co.zynafin.smokoo.auction.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeRemainingParserTests {
	
	@Test
	public void parse() throws Exception {
		//GIVEN
		String content = "^!%7894|1176022.435|<SPAN>R</SPAN>10.06|andre3610|open^!%9721|78902.435|<SPAN>R</SPAN>0.05|missbloch|open^!%9722|165292.435|<SPAN>R</SPAN>0.00||open^!%9724|632.435|<SPAN>R</SPAN>4.70|Lawrys|open^!%9725|86092.435|<SPAN>R</SPAN>0.00||open^!%9726|172492.435|<SPAN>R</SPAN>0.00||open^!%9728|71702.435|<SPAN>R</SPAN>0.05|hemi300c|open^!%9729|158092.435|<SPAN>R</SPAN>0.00||open^!%9730|4.435|<SPAN>R</SPAN>288.80|RobTheBrue|open^!%9731|57512.435|<SPAN>R</SPAN>1.10|Janvil|open^!%9733|50152.435|<SPAN>R</SPAN>0.30|catfish|open^!%9734|136492.435|<SPAN>R</SPAN>0.00||open^!%9736|46592.435|<SPAN>R</SPAN>0.50|strandshark|open^!%9738|64532.435|<SPAN>R</SPAN>0.20|clayherb|open^!%9739|150892.435|<SPAN>R</SPAN>0.00||open^!%9754|132892.435|<SPAN>R</SPAN>0.00||open^!%9759|143692.435|<SPAN>R</SPAN>0.00||open^!%10585|219292.435|<SPAN>R</SPAN>0.00||open^!%10586|230092.435|<SPAN>R</SPAN>0.00||open^!%10588|237292.435|<SPAN>R</SPAN>0.00||open^!%10589|244492.435|<SPAN>R</SPAN>0.00||open^!%10590|251692.435|<SPAN>R</SPAN>0.00||open^!%10591|258892.435|<SPAN>R</SPAN>0.00||open^!%10592|305692.435|<SPAN>R</SPAN>0.00||open^!%10596|323692.435|<SPAN>R</SPAN>0.00||open^!%10597|330892.435|<SPAN>R</SPAN>0.00||open^!%10599|316492.435|<SPAN>R</SPAN>0.00||open^!%10600|338092.435|<SPAN>R</SPAN>0.00||open^!%10602|345292.435|<SPAN>R</SPAN>0.00||open^!%10603|309292.435|<SPAN>R</SPAN>0.00||open^!%10604|222892.435|<SPAN>R</SPAN>0.00||open^!%10699|8.435|<SPAN>R</SPAN>0.34|dastags|open^!%10700|1132.435|<SPAN>R</SPAN>0.05|Mojo88|open^!%10701|3178.435|<SPAN>R</SPAN>0.00||open^!%10702|3488.435|<SPAN>R</SPAN>0.00||open";
		TimeRemainingParser parser = new TimeRemainingParser();
		//WHEN
		long time = parser.parse(content,10600l);
		assertEquals(338092435, time);
	}
	
	@Test
	public void parse_IAmTheUser() throws Exception {
		//GIVEN
		String content = "^!%7894|1176022.435|<SPAN>R</SPAN>10.06|andre3610|open^!%9721|78902.435|<SPAN>R</SPAN>0.05|bobjana|open^!%9722|165292.435|<SPAN>R</SPAN>0.00||open^!%9724|632.435|<SPAN>R</SPAN>4.70|Lawrys|open^!%9725|86092.435|<SPAN>R</SPAN>0.00||open^!%9726|172492.435|<SPAN>R</SPAN>0.00||open^!%9728|71702.435|<SPAN>R</SPAN>0.05|hemi300c|open^!%9729|158092.435|<SPAN>R</SPAN>0.00||open^!%9730|4.435|<SPAN>R</SPAN>288.80|RobTheBrue|open^!%9731|57512.435|<SPAN>R</SPAN>1.10|Janvil|open^!%9733|50152.435|<SPAN>R</SPAN>0.30|catfish|open^!%9734|136492.435|<SPAN>R</SPAN>0.00||open^!%9736|46592.435|<SPAN>R</SPAN>0.50|strandshark|open^!%9738|64532.435|<SPAN>R</SPAN>0.20|clayherb|open^!%9739|150892.435|<SPAN>R</SPAN>0.00||open^!%9754|132892.435|<SPAN>R</SPAN>0.00||open^!%9759|143692.435|<SPAN>R</SPAN>0.00||open^!%10585|219292.435|<SPAN>R</SPAN>0.00||open^!%10586|230092.435|<SPAN>R</SPAN>0.00||open^!%10588|237292.435|<SPAN>R</SPAN>0.00||open^!%10589|244492.435|<SPAN>R</SPAN>0.00||open^!%10590|251692.435|<SPAN>R</SPAN>0.00||open^!%10591|258892.435|<SPAN>R</SPAN>0.00||open^!%10592|305692.435|<SPAN>R</SPAN>0.00||open^!%10596|323692.435|<SPAN>R</SPAN>0.00||open^!%10597|330892.435|<SPAN>R</SPAN>0.00||open^!%10599|316492.435|<SPAN>R</SPAN>0.00||open^!%10600|338092.435|<SPAN>R</SPAN>0.00||open^!%10602|345292.435|<SPAN>R</SPAN>0.00||open^!%10603|309292.435|<SPAN>R</SPAN>0.00||open^!%10604|222892.435|<SPAN>R</SPAN>0.00||open^!%10699|8.435|<SPAN>R</SPAN>0.34|dastags|open^!%10700|1132.435|<SPAN>R</SPAN>0.05|Mojo88|open^!%10701|3178.435|<SPAN>R</SPAN>0.00||open^!%10702|3488.435|<SPAN>R</SPAN>0.00||open";
		TimeRemainingParser parser = new TimeRemainingParser();
		//WHEN
		long time = parser.parse(content,9721l);
		assertEquals(5000, time);
	}
}
