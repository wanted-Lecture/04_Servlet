package com.wanted.servlet.h_cookie_session;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/session")
public class SessionServlet extends HttpServlet {

    /* comment
    *   doPost : 사용자 입력 userId, userPwd를 세션에 저장하며, 유효시간 설정
    *   doGet : 저장 된 세션에서 값을 꺼내와 JSP로 전달하여 결과 표시
    * */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");

        // 세션 가져오는 구문
        HttpSession session = req.getSession(false); // 기존 세션이 없으면 null 반환
        String loggedInUser = null;
        String pwd = null;

        if(session != null) {
            loggedInUser = session.getAttribute("loggedInUser").toString();
            pwd = session.getAttribute("userPwd").toString();
        }

        req.setAttribute("loggedInUser", loggedInUser != null ? loggedInUser : "로그인 안됨!!!");
        req.setAttribute("pwd", pwd != null ? pwd : "비밀번호 없음");

        RequestDispatcher rd = req.getRequestDispatcher("/h_cookie_session/session-result.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");

        // 로그인 버튼을 눌렀을 때
        // id, pwd 값을 request 객체에서 꺼내 담기
        String userId = req.getParameter("userId");
        String userPwd = req.getParameter("userPwd");

        if (userId != null && userPwd != null) {
            // 사용자의 아이디어와 비밀번호를 저장하기 위한 Session 객체 생성
            // true로 설정하면 세션 객체를 생성한다는 뜻.
            HttpSession session = req.getSession(true);

            // 동일한 사용자인지를 확인하기 위한 변수 생성
            String existUser = (String) session.getAttribute("loggedInUser");

            if (existUser == null || existUser.equals(userId)) {

                session.setAttribute("loggedInUser", userId);
                if (userPwd != null && !userPwd.isEmpty()) {
                    session.setAttribute("userPwd", userPwd);
                }
                session.setMaxInactiveInterval(30); // session의 유효 기간 설정을 30초 설정
            } else {
                session.setAttribute("loggedInUser", userId);
                if (userPwd != null && !userPwd.isEmpty()) {
                    session.setAttribute("userPwd", userPwd);
                }
                session.setMaxInactiveInterval(30); // session의 유효 기간 설정을 30초 설정
            }

        }

        doGet(req, resp);
    }
}
