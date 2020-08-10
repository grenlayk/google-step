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

package com.google.sps;

import java.util.Collections;
import java.util.Collection;

import java.util.ArrayList;
import com.google.sps.Event;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> allEvents, MeetingRequest request) {
    ArrayList<Event> relatedEvents = leaveRelated(allEvents, request);
    relatedEvents.sort(Comparator.comparing(event -> event.getWhen().start()));
    int endOfLast = TimeRange.START_OF_DAY;
    
    ArrayList<TimeRange> availableSlots = new ArrayList<TimeRange> ();
    for (Event event : relatedEvents) {
        if (event.getWhen().start() - endOfLast >= request.getDuration()) {
            availableSlots.add(TimeRange.fromStartEnd(endOfLast, event.getWhen().start(), false));
        }
        endOfLast = Math.max(event.getWhen().end(), endOfLast);
    }

    if (TimeRange.END_OF_DAY - endOfLast >= request.getDuration()) {
        availableSlots.add(TimeRange.fromStartEnd(endOfLast, TimeRange.END_OF_DAY, true));
    }

    return availableSlots;
  }

  public ArrayList<Event> leaveRelated(Collection<Event> allEvents, MeetingRequest request) {
      ArrayList<Event> relatedEvents = new ArrayList<>();
      for (Event event : allEvents) {
          if (!intersection(event.getAttendees(), request.getAttendees()).isEmpty()) {
              relatedEvents.add(event);
          }
      }
      return relatedEvents;
  }

  Set<String> intersection(Collection<String> event, Collection<String> request) {
    Set<String> intersection = new HashSet<String>(event); 
    Set<String> requestSet = new HashSet<String>(request); 
    intersection.retainAll(requestSet);
    return intersection;
  }
}
