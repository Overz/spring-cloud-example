package com.example.springcloud.application;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/hello")
@RestController
@AllArgsConstructor
public class Controller {
  private final Properties properties;

  @GetMapping
  public ResponseEntity<Map<String, Object>> get(
      @RequestParam(name = "name", required = false) String name
  ) {
    if (name == null) name = properties.getDefaultValue();

    Map<String, Object> map = new HashMap<>();
    map.put("message", properties.getGreeting() + name);
    map.put("date", new Date());
    return ResponseEntity.ok(map);
  }
}
