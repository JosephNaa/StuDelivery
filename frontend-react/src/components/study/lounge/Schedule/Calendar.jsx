import React from "react";
import { useSelector } from "react-redux";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import styled from "styled-components";
// import "@fullcalendar/core/main.css";
import "@fullcalendar/daygrid/main.css";

// import "./Calendar.css";

function Calendar({ schedules }) {
  const study = useSelector((state) => state.study.study);

  const scheduleEvent = (schedules) => {
    const events = [];
    if (schedules) {
      for (var i in schedules) {
        // console.log(schedules[i]);
        events.push({
          title: schedules[i].title,
          date: schedules[i].time,
          backgroundColor: "rgb(0, 185, 186)",
          textColor: "#000000",
          // display: "background",
          // backgroundColor: "#00b9ba",
        });
      }
      return events;
    }
  };
  // console.log(scheduleEvent(schedules));
  return (
    <div>
      <FullCalendar
        defaultView="dayGridMonth"
        plugins={[dayGridPlugin]}
        // weekends={false}
        height={600}
        events={scheduleEvent(schedules)}
        locale="ko"
        minDate={study.start_at}
        color="yellow"
        textColor="black"
        eventBackgroundColor="#ffffff"
      />
    </div>
  );
}
export default Calendar;
