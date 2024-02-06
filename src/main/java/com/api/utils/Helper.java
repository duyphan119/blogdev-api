package com.api.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class Helper {
    public String generateSlug(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String slug = pattern.matcher(normalized)
                .replaceAll("")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\-]", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
        return slug;
    }

    public int generateRandomNumber(int maxValue) {
        Random random = new Random();
        int randomNumber = random.nextInt(maxValue + 1);
        return randomNumber;
    }

    public Pageable generatePageable(Integer limit, Integer page, String sortBy, String sortType) {
        Sort sort = Sort.by(sortBy);
        if (sortType.toLowerCase().equals("desc")) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        return pageable;
    }
}
