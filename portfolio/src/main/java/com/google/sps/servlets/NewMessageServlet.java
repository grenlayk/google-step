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
import java.util.List;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/new-message")
public class NewMessageServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String userName = request.getParameter("user_name");
    String userMessage = request.getParameter("user_message");
    long timestamp = System.currentTimeMillis();
    System.err.println("Got there");

    UserMessage curMessage = new UserMessage(userName, userMessage);

    if (!curMessage.isCorrect()) {
      System.err.println("Empty name or message field");
    } else {
      System.err.println("Have message");
      Entity messageEntity = new Entity("userMessage");
      messageEntity.setProperty("userName", userName);
      messageEntity.setProperty("userMmessage", userMessage);
      messageEntity.setProperty("timestamp", timestamp);

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(messageEntity);
    }

    // Redirect back to the HTML page.
    response.sendRedirect("/chat.html");
  }

}
