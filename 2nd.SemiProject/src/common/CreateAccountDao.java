package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class CreateAccountDao  {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	DBConnectionMgr dbMgr = null;
	
	public boolean insert(CreateAccountVO caVO){//오라클에  회원 정보를 넣는 부분
		boolean sql_insertcheck;
		StringBuilder sql_insert = new StringBuilder();//sql문을 쓸 때 String보다는 StringBuilder가 싱글스레드에서 안전하게 작동함.
		sql_insert.append("insert into"); //append()가 저 문자를 추가하겠다라는 의미임.
		sql_insert.append(" account_info(SELECT seq_member_code.NEXTVAL, ? , ? , ? , ? , ? , ? FROM dual"); //여기 적히는 문장이 sql에서 직접 적는 부분에 자동으로 입력됨.
		sql_insert.append(" WHERE not exists(SELECT * FROM account_info WHERE id = ?)) "); //sequence 실행조건 nextval을 입력해야 초기값을 저장하기 시작하면서 구동됨.
		dbMgr = DBConnectionMgr.getInstance(); //싱글톤 패턴으로 서버 연결은 이걸로 하면 연결에 필요한 데이터를 받음.
		try {
			con = dbMgr.getConnection(); //데이터를 통해서 sql연결
			pstmt = con.prepareStatement(sql_insert.toString()); //쿼리문을 실행하는 문장(sql_insert가 stringbuffer 타입(string 상속받은 친구)인데 string을 원해서 toString()전환함.)
			int i=1;//각각 컬럼마다 1,2,3,4순서로 인식함.
			pstmt.setString(i++, caVO.getId()); //? 순서에 맞춰서 넣어들어감. i는 1,2,3,4,5,6,7로 인식 후 새로 추가하면 1,2,3,4,5,6,7로 계속 진행됨. - i는 직관적으로 쓰기 위해서 선언한것.
			pstmt.setString(i++, caVO.getPw());
			pstmt.setString(i++, caVO.getName());
			pstmt.setString(i++, caVO.getDeptno());
			pstmt.setString(i++, caVO.getGender());
			pstmt.setString(i++, caVO.getEmail());
			pstmt.setString(i++, caVO.getId());
		} catch (SQLException se) {
			System.out.println("[[sql_insert]]" +sql_insert); //쿼리 안되면 예외
		} catch (Exception e) {
			e.printStackTrace(); // 기타 여러 문제
		} 
		try {
			int result = pstmt.executeUpdate(); //executeUpdate()는 영향을 받는 row(세로)줄을 숫자로 표시함. 예를 들어 추가 되면 1개의 로우가 반응되어 1의  값을 가짐. 실패하면 0을 받음.
				if(result > 0) {
					System.out.println("ok");
					sql_insertcheck = true;
					return sql_insertcheck;
				}else {
					System.out.println("최종 등록 실패");
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbMgr.freeConnection(con, pstmt); //사용한 건 자원 반납.
		}
		return false;
	}
	
	public boolean overlapID(CreateAccountVO caVO) {//아이디 중복검사 실시 메소드
		StringBuilder sql_overlap = new StringBuilder();
		sql_overlap.append("SELECT nvl((SELECT 1 FROM dual WHERE EXISTS (SELECT id FROM account_info WHERE id = ?)), 0) result");
		sql_overlap.append(" FROM dual");
		dbMgr = DBConnectionMgr.getInstance();
		try {
			con=dbMgr.getConnection();
			//프로시저로 만들었다고 가정하면
			//pstmt = con.prepareCalll("{call proc_IDOverlap(?, ?)}");
			pstmt = con.prepareStatement(sql_overlap.toString());
			int i = 1;
			pstmt.setString(i++, caVO.getId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int result = rs.getInt("result");
				System.out.println(result);
				if(result > 0) {
					return true;
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			dbMgr.freeConnection(con, pstmt, rs);
		}
		return false;
		
	} 
}
