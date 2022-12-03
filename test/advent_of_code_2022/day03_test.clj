(ns advent-of-code-2022.day03-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day03 :refer [part1 part2]]))

(def supplies-example ["vJrwpWtwJgWrhcsFMMfFFhFp"
                       "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL"
                       "PmmdzqPrVvPwwTWBwg"
                       "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn"
                       "ttgJtRGJQctTZtZT"
                       "CrZsJsPPZsGzwwsLwLmpwMDw"])

(deftest part1-test
  (testing "Part 1"
    (is (= 157 (part1 supplies-example)))))

(deftest part2-test
  (testing "Part 2"
    (is (= 70 (part2 supplies-example)))))
