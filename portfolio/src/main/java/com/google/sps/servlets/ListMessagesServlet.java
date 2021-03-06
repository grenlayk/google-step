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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;


@WebServlet("/list-messages")
public class ListMessagesServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("userMessage").addSort("timestamp", SortDirection.DESCENDING);
    int maxMessages = 5;

    String maxParam = request.getParameter("max_messages");
    try {
        maxMessages = Integer.parseInt(maxParam); 
    } 
    catch(Exception e) {
        System.out.println("Wrong input");
    }

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<UserMessage> messages = new ArrayList<>();
    int messageNum = 0;

    for (Entity entity : results.asIterable()) {
      if (messageNum < maxMessages) {
        Long id = entity.getKey().getId();
        String userMessage = (String) entity.getProperty("userMessage");
        String userEmail = (String) entity.getProperty("userEmail");
        Long timestamp = (long) entity.getProperty("timestamp");

        UserMessage message = new UserMessage(userMessage, userEmail, id, timestamp);
        messages.add(message);
      }
      ++messageNum;
    }

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(messages));
  }
}
