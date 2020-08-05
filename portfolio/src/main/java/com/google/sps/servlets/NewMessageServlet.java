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

import java.io.IOException;
import java.util.ArrayList;
import com.google.sps.data.UserMessage;
import com.google.sps.data.UserMessageError;
import java.util.List;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;


@WebServlet("/new-message")
public class NewMessageServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    UserService userService = UserServiceFactory.getUserService();
    
    if (!userService.isUserLoggedIn()) {
        response.sendRedirect("/user-info");
        return;
    }

    String userMessage = request.getParameter("user_message");
    long timestamp = System.currentTimeMillis();

    UserMessage curMessage = new UserMessage(userMessage);
    String logMessage = curMessage.check().getError();

    if (logMessage != null) {
      System.err.println(logMessage);

      response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, logMessage);
    } else {
      
      String userEmail = userService.getCurrentUser().getEmail();

      Entity messageEntity = new Entity("userMessage");
      messageEntity.setProperty("userMessage", userMessage);
      messageEntity.setProperty("timestamp", timestamp);
      messageEntity.setProperty("userEmail", userEmail);

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      
      datastore.put(messageEntity);

      // Redirect back to the HTML page.
      response.sendRedirect("/chat.html");
    }
  }

}
