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

async function loadMessages() {
  const maxEl = document.getElementById('max');
  const maxText = maxEl.options[maxEl.selectedIndex].text;

  const response = await fetch('/list-messages?max_messages=' + maxText);
  const serverMessages = await response.json();

  const messagesEl = document.getElementById('users-messages');
  messagesEl.innerHTML = '';
  for (const message of serverMessages) {
    const curMessage = document.createElement('p');
    curMessage.appendChild(createMyElement(message.userName + ': ', 'b'));
    curMessage.appendChild(createMyElement(message.userMessage, 'bdi'));

    messagesEl.appendChild(curMessage);
  }
}

async function deleteMessages() {
  const response = await fetch('/delete-messages');
}

function createMyElement(text, type) {
  const element = document.createElement(type);
  element.innerText = text;
  return element;
}

/** Creates a map and adds it to the page. */
function createMap() {
  var styleOptions = [
        {
            "featureType": "administrative.country",
            "elementType": "geometry.fill",
            "stylers": [
            {
                "visibility": "off"
            }
            ]
        },
        {
            "featureType": "landscape.natural",
            "elementType": "geometry",
            "stylers": [
            {
                "color": "#edf5ef"
            }
            ]
        }
        ];
  var info = {
      "costa_rica": {
          "lat": 9.932955,
          "lng": -84.079496,
          "title": "Costa Rica",
          "description": "World Robot Olympiad took place here in 2017"
      },
      "china": {
          "lat": 39.915149, 
          "lng": 116.384680,
          "title": "China",
          "description": "World Adolescent Robot Contest took place here in 2015"
      },
      "qatar": {
          "lat": 25.293616, 
          "lng": 51.517145,
          "title": "Qatar",
          "description": "World Robot Olympiad took place here in 2015"
      },
      "tailand": {
          "lat": 18.799605, 
          "lng": 98.981883,
          "title": "Thailand",
          "description": "World Robot Olympiad took place here in 2018"
      },
      "india": {
          "lat": 28.626527, 
          "lng": 77.192504,
          "title": "India",
          "description": "World Robot Olympiad took place here in 2016"
      },
      "philippines": {
          "lat": 10.292572, 
          "lng": 123.961117,
          "title": "Philippines",
          "description": "World Robot Olympiad Friendship Invitational took place here in 2018"
      },
      "indonesia": {
          "lat": -6.210629, 
          "lng": 106.826769,
          "title": "Indonesia",
          "description": "World Robot Olympiad took place here in 2013"
      }
  }
  const countryEl = document.getElementById('country');
  const countryValue = countryEl.options[countryEl.selectedIndex].value;
  var mapOptions = {
  zoom: 8,
  center: {lat:info[countryValue].lat, lng: info[countryValue].lng},
  styles: styleOptions
  };
  const map = new google.maps.Map(
      document.getElementById('map'),  
      mapOptions
  );
  
  for (var countryKey in info) {
    addLandmark(
      map, info[countryKey].lat, info[countryKey].lng, info[countryKey].title,
      info[countryKey].description);
  }
  
}


/** Adds a marker that shows an info window when clicked. */
function addLandmark(map, lat, lng, title, description) {
  const marker = new google.maps.Marker({
      position: {lat: lat, lng: lng}, 
      map: map, 
      title: title,
      animation: google.maps.Animation.DROP,
    });

  const infoWindow = new google.maps.InfoWindow({content: description});
  marker.addListener('click', () => {
    infoWindow.open(map, marker);
  });
}
