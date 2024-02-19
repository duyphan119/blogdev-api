package com.api.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            return userRepo.findByEmail(email);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> create(User user) {
        try {
            return Optional.ofNullable(userRepo.save(user));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> update(User user) {
        try {
            return Optional.ofNullable(userRepo.save(user));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public long countAll() {
        return userRepo.count();
    }

    @Override
    public Author convertUserToAuthor(User user) {
        return Author.builder()
                .email(user.getEmail())
                .twitterUrl(user.getTwitterUrl())
                .facebookUrl(user.getFacebookUrl())
                .youtubeUrl(user.getYoutubeUrl())
                .lastName(user.getLastName())
                .linkedinUrl(user.getLinkedinUrl())
                .githubUrl(user.getGithubUrl())
                .firstName(user.getFirstName())
                .fullName(user.getFullName())
                .introduction(user.getIntroduction())
                .id(user.getId())
                .pinterestUrl(user.getPinterestUrl())
                .imageUrl(user.getImageUrl())
                .career(user.getCareer())
                .build();
    }

    @Override
    public User convertAuthorToUser(Author author) {
        return User.builder()
                .email(author.getEmail())
                .twitterUrl(author.getTwitterUrl())
                .facebookUrl(author.getFacebookUrl())
                .youtubeUrl(author.getYoutubeUrl())
                .lastName(author.getLastName())
                .linkedinUrl(author.getLinkedinUrl())
                .githubUrl(author.getGithubUrl())
                .firstName(author.getFirstName())
                .introduction(author.getIntroduction())
                .id(author.getId())
                .pinterestUrl(author.getPinterestUrl())
                .imageUrl(author.getImageUrl())
                .career(author.getCareer())
                .build();
    }

    @Override
    public Optional<Author> getAuthor(Long id) {
        Optional<User> userOptional = this.userRepo.findById(id);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.convertUserToAuthor(userOptional.get()));
    }

}
