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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;


@WebServlet("/delete-message")
public class DeleteMessageServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Long id = Long.parseLong(request.getParameter("id"));
    String messageUserEmail = null;

    Key messageEntityKey = KeyFactory.createKey("userMessage", id);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    UserService userService = UserServiceFactory.getUserService();

    try {
        Entity message = datastore.get(messageEntityKey);
        messageUserEmail = (String) message.getProperty("userEmail");
    } catch (EntityNotFoundException e){
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No such message in datastore");
        return;
    }

    if (!userService.isUserLoggedIn())  {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("You should log in to delete messages");
        return;
    }

    String userEmail = userService.getCurrentUser().getEmail();

    if (userEmail.equals(messageUserEmail)) {
      datastore.delete(messageEntityKey);
    }

    response.sendRedirect("/chat.html");
  }
}
