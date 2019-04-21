package com.testtask;

import com.opencsv.CSVReader;
import com.testtask.domain.Form;
import com.testtask.repos.FormRepo;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TestController {

    @Autowired
    private FormRepo formRepo;

    @GetMapping
    public String greeting(
            @RequestParam(name="name", required=false, defaultValue="World") String name,
            Map<String, Object> model
    ) {
        model.put("name", name);
        return "greeting";
    }

    @GetMapping("/import")
    public String importforms(
            Map<String, Object> model
    ) throws IOException {
        formRepo.deleteAll();
        ArrayList<Form> forms = new ArrayList<Form>();

        FileReader reader = new FileReader("src/main/resources/csv/test_case.csv");

        CSVReader csvReader = new CSVReader(reader);

        List<String[]> lines = csvReader.readAll();
        lines.forEach(line -> {
            try {
                forms.add(new Form(
                        line[0],
                        Integer.parseInt(line[1]),
                        line[2],
                        line[3],
                        line[4],
                        line[5],
                        line[6],
                        line[7],
                        line[9],
                        line[10],
                        new SimpleDateFormat("yyyy-MM-dd-hh").parse(line[11])
                ));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        formRepo.saveAll(forms);
        return "import";
    }

    @GetMapping("/not-finished")
    public String notFinished(Map<String, Object> model) {
        List<Form> forms = new ArrayList<>();
        formRepo.findAll().iterator().forEachRemaining(forms::add);


        Map<List<String>, String> lastActionOfSession = new HashMap<>();
        forms.stream().filter(form -> !form.getSubtype().equals("")
                && !form.getType().equals("")
                && !form.getFormid().equals("")
                && !form.getGrp().equals("")
                && !form.getSsoid().equals(""))
            .collect(Collectors.toList())
            .forEach(form -> {
                List<String> criteria = new ArrayList<>();
                criteria.add(form.getSsoid());
                criteria.add(form.getFormid());
                criteria.add(form.getGrp());
                criteria.add(form.getType());
                lastActionOfSession.put(criteria, form.getSubtype());
            });

        Set<Map.Entry<List<String>, String>> entrySet = lastActionOfSession.entrySet();
        List<String> summary = entrySet.stream()
            .filter(entry -> !entry.getValue().equals("send") &&
                    !entry.getValue().equals("sent") &&
                    !entry.getValue().equals("done") &&
                    !entry.getValue().equals("success") &&
                    !entry.getValue().equals("change") &&
                    !entry.getValue().equals("selected")
            )
            .map(entry -> {
                return "Пользователь " + entry.getKey().get(0) + " работал с формой " + entry.getKey().get(1) +
                        "(" + entry.getKey().get(2) + "). Последнее сообщение: " + entry.getValue();
        }).collect(Collectors.toList());

        model.put("data", summary);
        model.put("count", summary.size());
        return "not-finished";
    }

    @GetMapping("/top-five")
    public String topFive(Map<String, Object> model) {
        List<Form> forms = new ArrayList<>();
        formRepo.findAll().iterator().forEachRemaining(forms::add);

        Map<String, Integer> form2Count = new HashMap<>();
        forms.stream().forEach(form -> {
            if (form2Count.containsKey(form.getFormid())) {
                form2Count.put(form.getFormid(), Integer.valueOf(form2Count.get(form.getFormid()) + 1));
            } else {
                form2Count.put(form.getFormid(), 1);
            }
        });

        List<Pair<Integer, String>> sorted = form2Count.entrySet().stream().map(entry -> Pair.of(entry.getValue(), entry.getKey())).collect(Collectors.toList());
        Collections.sort(sorted);
        Collections.reverse(sorted);
        sorted = sorted.stream().filter(entry -> !entry.getRight().equals("")).collect(Collectors.toList());

        List<String> summary = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            summary.add("Id формы " + sorted.get(i).getRight() + ". Использовалась " + sorted.get(i).getLeft() + " раз");
        }

        model.put("list", summary);
        return "top-five";
    }

    @GetMapping("/lasthour")
    public String lasthour(
            Map<String, Object> model
    ) {
        List<Form> forms = new ArrayList<>();
        formRepo.findAll().iterator().forEachRemaining(forms::add);

        Date lastHour = forms.stream().map(Form::getYmdh).max(Date::compareTo).get();

        forms = forms.stream().filter(form -> form.getYmdh().equals(lastHour)).collect(Collectors.toList());

        Map<String, List<String>> user2Form = new HashMap<>();

        forms.stream().forEach(form -> {
            if (user2Form.containsKey(form.getSsoid())) {
                user2Form.get(form.getSsoid()).add(form.getFormid());
            } else {
                List<String> list = new ArrayList<>();
                list.add(form.getFormid());
                user2Form.put(form.getSsoid(), list);
            }
        });

        Set<Map.Entry<String, List<String>>> entrySet = user2Form.entrySet();

        model.put("lastHour", lastHour);
        model.put("data", entrySet);
        return "lasthour";
    }
}