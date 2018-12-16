package servlet;


import base.AddressDataSet;
import base.PhoneDataSet;
import base.UserDataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Component
public class AdminServlet extends HttpServlet {

    private static final String DEFAULT_USER_NAME = "UNKNOWN";
    private static final String ADMIN_PAGE_TEMPLATE = "admin.html";
    public static final String RESULT = "result";
    public static final String USERID = "userid";
    public static final String USERNAME = "username";
    public static final String AGE = "age";
    public static final String ADDRESS = "address";
    public static final String PHONE = "phone";
    public static final String IS_SHOWNUMBER = "shownumber";

    @Autowired
    private TemplateProcessor templateProcessor;


    Map<String, Object> pageVariables = new HashMap<>();


    public AdminServlet(TemplateProcessor templateProcessor /*,DBService dbService*/) {
        this.templateProcessor = templateProcessor;
    }


    @Override
    public void init() throws ServletException {
        super.init();
    }


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        UserDataSet userDataSetResult = null;
        if (request.getParameter(USERID) != null) {
            int userid = Integer.parseInt(request.getParameter(USERID));
            Map<String, Object> params = new HashMap<>();
            params.put("USERID", userid);
        }


        if (userDataSetResult == null) {
            pageVariables.put(RESULT, "");
        }

        if (request.getParameter(IS_SHOWNUMBER) != null) {

            pageVariables.put(RESULT, "Кол-во записей: ");
        }

        response.setContentType("text/html;charset=utf-8");
        String page = templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, pageVariables);
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        String name = request.getParameter(USERNAME);
        if (name == null) {
            pageVariables.put(RESULT, "Пользователь успешно добавлен!");
        }
        int age = Integer.parseInt(request.getParameter(AGE));
        AddressDataSet address = new AddressDataSet(request.getParameter(ADDRESS));

        String[] phoneArr = request.getParameterValues(PHONE);
        List<PhoneDataSet> list = new ArrayList<>();
        for (int i = 0; i < phoneArr.length; i++) {
            list.add(new PhoneDataSet(phoneArr[i]));
        }
        UserDataSet userDataSetExpected = new UserDataSet(name, age, address, list);
        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        Map<String, Object> params = new HashMap<>();
        params.put("USERDATASET", userDataSetExpected);
        pageVariables.put(RESULT, "Пользователь успешно добавлен!");
        response.setContentType("text/html;charset=utf-8");
        String page = templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, pageVariables);
        response.getWriter().println(page);
        setOK(response);

    }


    private void setOK(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}