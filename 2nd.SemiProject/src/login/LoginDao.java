package login;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.CreateAccountVO;
import common.DBConnectionMgr;
public class LoginDao {
	//DB연결을 위한 필요변수 선언
	Connection con = null;
	CallableStatement cstmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	DBConnectionMgr dbMgr = null;

	public String login(String p_id, String p_pw) {//로그인을 위한 dao
		String msg = ""; //밑에 3번을 받을 msg라는 변수를 선언
		dbMgr = DBConnectionMgr.getInstance(); //싱글톤패턴으로 DB와 연동될 재료들 가져옴(ip, id, pw 등)
		try {
			con = dbMgr.getConnection();//연결시도
			cstmt = con.prepareCall("{call proc_login(?,?,?)}"); //연결 상태에서 프로시저 실행 - 프로시저는 call로 진행
			cstmt.setString(1, p_id);//프로시저 첫번째 자리에 id를 String으로 넣는 것이라고 인식
			cstmt.setString(2, p_pw);//프로시저 두번째 자리에 pw를 String으로 넣는 것이라고 인식
			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR); //3번째 프로시저 자리에 출력 될 때 VARCHAR(자바에선 String)로 출력한다고 인식.
			int result = 0; //프로시저가 수행하고 나오는 결과를 인식하는데 숫자값으로 받기 위해서 result를 하나 선언함.
			result = cstmt.executeUpdate();//executeUpdate는 영향을 받은 row를 숫자로 리턴값을 받음. 여기서는 지금 일치여부라서 영향을 주는 건 우리가 입력한 id, pw와 연관된 회원정보 1개뿐임.
			if(result==1) {
				msg = cstmt.getString(3); //msg가 문자열로 3번에 값을 받음(프로시저에서 아무 이상 없으면 이름을 받는 것으로 인식시켰음)
				System.out.println("연결된 결과(성공 시 이름, 실패 시 에러) ===> "+cstmt.getString(3));
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbMgr.freeConnection(con, cstmt); //프로시저 사용한 것과 연결 정보등을 다시 반납함(일종의 종료)
		} return(msg);//이 프로시저의 값은 msg의 값(프로시저에 저장된 논리의 값 - 다 맞으면 이름, id틀리면 "아이디가 존재하지 않습니다.", pw틀리면 "비밀번호가 다릅니다")을 가지게 된다. - 여기서는 다시 view로 넘겨줌(호출된게 거기뿐이라서)
	}
	
	public String email(CreateAccountVO caVO) {//로그인 대상의 이메일을 가져오는 메소드
		StringBuilder sql_email = new StringBuilder();
		sql_email.append("SELECT email");
		sql_email.append(" FROM account_info");		
		sql_email.append(" WHERE id = ?");		
		dbMgr = DBConnectionMgr.getInstance();
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql_email.toString());
			int i = 1;
			pstmt.setString(i++, caVO.getId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String email = rs.getString("email");
				return email;
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbMgr.freeConnection(con, pstmt, rs);
		}
		return null;
	}

}
