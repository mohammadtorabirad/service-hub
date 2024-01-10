package ir.co.kishsys.sanhabinquiry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SanhabInquiryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SanhabInquiryApplication.class, args);
	}

}






//AgncFldCod not null it is int ---- request error
//<ns:CrkGuid></ns:CrkGuid>
//<ns:CrkSeri></ns:CrkSeri>
//CrkSrl not null it is int ---- request error
//<ns:CusAccLglId></ns:CusAccLglId>
//<ns:CusAccLglNam></ns:CusAccLglNam>
//CusAccLicTypCod enum code not description
//PlkCod  not null it is int ---- request error
//<ns:CusAccUnIrnCod></ns:CusAccUnIrnCod>
//<ns:CusCertGuid></ns:CusCertGuid>
//<ns:LosEvlLglId></ns:LosEvlLglId>