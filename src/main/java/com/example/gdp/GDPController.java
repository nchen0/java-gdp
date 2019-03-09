package com.example.gdp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class GDPController {
    private final GDPRepository repos;
    private final RabbitTemplate rt;

    public GDPController(GDPRepository repos, RabbitTemplate rt) {
        this.repos = repos;
        this.rt = rt;
    }

    @GetMapping("/countries")
    public List<GDP> all() {
        return repos.findAll();
    }

    @GetMapping("/names")
    public List<GDP> listByNames() {
        return repos.findAll()
                .stream()
                .sorted((c1, c2) -> c1.getCountry().compareToIgnoreCase(c2.getCountry()))
                .collect(Collectors.toList());
    }

    @GetMapping("/economy")
    public List<GDP> getEconomy() {
        // Some comparators like (person1, person2) -> person1.getName().compareTo(person2.getName()) could be simplified like this: Comparator.comparing(Person::getName).
        return repos.findAll()
                .stream()
                .sorted(Comparator.comparing(GDP::getGdp, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @GetMapping("/total")
    public ObjectNode getTotal() {
        List<GDP> countries = repos.findAll();

        Long total = 0L;
        for (GDP c : countries) {
            total += c.getGdp();
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode totalGDP = mapper.createObjectNode();
        totalGDP.put("id", 0);
        totalGDP.put("gdp", "total");
        totalGDP.put("GDP", total);

        return totalGDP;
    }

    @GetMapping("/gdp/{country}")
    public GDP findOne(@PathVariable String country) {
        GDPLog message = new GDPLog("Checked country using name");
        log.info("Message has been sent");
        rt.convertAndSend(GdpApplication.QUEUE_NAME, message.toString());
        return repos.findByCountry(country);
    }

    @PostMapping("/gdp")
    public List<GDP> newCountry(@RequestBody List<GDP> newCountry) {
        return repos.saveAll(newCountry);
    }

}
