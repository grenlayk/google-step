// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

@WebServlet("/user-info")
public class UserInfoServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json;");

    UserService userService = UserServiceFactory.getUserService();
    JSONObject userInfo = new JSONObject();
    if (userService.isUserLoggedIn()) {
      String userEmail = userService.getCurrentUser().getEmail();
      String urlAfterLogOut = "/chat.html";
      String logoutUrl = userService.createLogoutURL(urlAfterLogOut);

      userInfo.put("email", userEmail);
      userInfo.put("loginUrl", null);
      userInfo.put("logoutUrl", logoutUrl);
      
    } else {
      String urlToRedirectToAfterUserLogsIn = "/chat.html";
      String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

      userInfo.put("email", null);
      userInfo.put("loginUrl", loginUrl);
      userInfo.put("logoutUrl", null);
    }

    response.getWriter().println(userInfo);
  }
}
