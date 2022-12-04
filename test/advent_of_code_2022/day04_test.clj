(ns advent-of-code-2022.day04-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day04 :refer [part1 part2]]))

(def pairs-example '("2-4,6-8"
                      "2-3,4-5"
                      "5-7,7-9"
                      "2-8,3-7"
                      "6-6,4-6"
                      "2-6,4-8"))

(deftest part1-test
  (testing "Part 1"
    (is (= 2 (part1 pairs-example)))))

(deftest part2-test
  (testing "Part 2"
    (is (= 4 (part2 pairs-example)))))
