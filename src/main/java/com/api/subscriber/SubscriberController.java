package com.api.subscriber;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.utils.ApiConstant;
import com.api.utils.ApiResponse;
import com.api.utils.PaginatedData;

@RestController
@RequestMapping("/subscriber")
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    @GetMapping()
    public ResponseEntity<Object> getSubscriberList(
            @RequestParam(name = "limit", defaultValue = "10") String limit,
            @RequestParam(name = "p", defaultValue = "1") String page,
            @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
            @RequestParam(name = "sort_type", defaultValue = "desc") String sortType,
            @RequestParam(name = "q", defaultValue = "") String keyword) {
        Page<Subscriber> subscriberPage = subscriberService.paginate(Integer.parseInt(limit), Integer.parseInt(page),
                sortBy,
                sortType, keyword);
        return ResponseEntity.status(ApiConstant.STATUS_200)
                .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                        .data(PaginatedData.<Subscriber>builder()
                                .rows(subscriberPage.getContent())
                                .totalPages(subscriberPage.getTotalPages())
                                .count(subscriberPage.getTotalElements())
                                .build())
                        .build());
    }

    @PostMapping()
    public ResponseEntity<Object> createSubscriber(@RequestBody Subscriber subscriber) {
        Optional<Subscriber> subscriberOptional = this.subscriberService.create(subscriber);

        if (subscriberOptional.isPresent()) {
            return ResponseEntity.status(ApiConstant.STATUS_201)
                    .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                            .data(subscriberOptional.get())
                            .build());
        }

        return ResponseEntity.status(ApiConstant.STATUS_500)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                        .data("Something went wrong")
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSubscriber(@PathVariable("id") Long id) {
        Boolean isDeleted = this.subscriberService.delete(id);
        if (isDeleted) {
            return ResponseEntity.status(ApiConstant.STATUS_201)
                    .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                            .data("Deleted")
                            .build());
        }

        return ResponseEntity.status(ApiConstant.STATUS_500)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                        .data("Something went wrong")
                        .build());
    }
}
