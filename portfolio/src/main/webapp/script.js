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

async function getMessage() {
  const response = await fetch('/data');
  const json = await response.json();

  const serverMessages = json.messages;
  const logMessage = json.logMessage;
  console.log(serverMessages);

  if (logMessage != "OK") {
    const warn = document.getElementById('warning-container');
    warn.innerHTML = '';
    warn.appendChild(createElement("Warning: " + logMessage, 'p'));
  }
  
  const messagesEl = document.getElementById('users-messages');
  messagesEl.innerHTML = '';
  for (const message of serverMessages) {
    messagesEl.appendChild(createElement(message.userName, 'h3'));
    messagesEl.appendChild(createElement(message.userMessage, 'p'));
  }
}

function createElement(text, type) {
  const element = document.createElement(type);
  element.innerText = text;
  return element;
}
