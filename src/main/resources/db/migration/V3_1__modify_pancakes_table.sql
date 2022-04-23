-- number of pancake entries might overflow INT datatype so we will use BIGINT
ALTER TABLE pancakes
    MODIFY id BIGINT NOT NULL AUTO_INCREMENT;