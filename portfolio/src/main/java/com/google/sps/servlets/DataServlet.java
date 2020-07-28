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


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private final List<UserMessage> messages = new ArrayList<UserMessage>();
  private String logMessage = "OK";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    String json = new Gson().toJson(this);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // // Get the input from the form.
    String userName = request.getParameter("user_name");
    String userMessage = request.getParameter("user_message");

    UserMessage curMessage = new UserMessage();
    curMessage.addNew(userName, userMessage);
    logMessage = "OK";

    if (!curMessage.isCorrect()) {
      System.err.println("Empty name or message field");
      logMessage = "Empty name or message field";
      response.sendRedirect("/chat.html");
      return;
    }

    messages.add(curMessage);

    // Redirect back to the HTML page.
    response.sendRedirect("/chat.html");
  }

}
