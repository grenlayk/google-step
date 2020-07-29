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

package com.google.sps.data;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Class representing the subtraction game, where players take turns subtracting from 21 to reach 0.
 *
 * <p>Note: The private variables in this class are converted into JSON.
 */
public class UserMessage {

  private String userName = "";
  private String userMessage = "Hello!";
  private long id;
  private long timestamp;

  public UserMessage(String name, String message) {
      this.userName = name;
      this.userMessage = message;
  }

  public boolean isCorrect() {
    if (userName == null || isEmpty(userName)) {
      return false;
    }
    if (userMessage == null || isEmpty(userMessage)) {
      return false;
    }
    return true;
  }

  // Filter for special HTML characters to prevent command injection attack
  private static String htmlFilter(String message) {
    return StringEscapeUtils.escapeHtml4(message);
  }

  private static boolean isEmpty(String message) {
    return htmlFilter(message.trim()).length() == 0;
  }

}
