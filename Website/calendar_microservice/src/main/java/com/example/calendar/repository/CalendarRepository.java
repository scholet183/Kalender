package com.example.calendar.repository;

import com.example.calendar.eto.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CalendarRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByUserId(Integer userId);
}
