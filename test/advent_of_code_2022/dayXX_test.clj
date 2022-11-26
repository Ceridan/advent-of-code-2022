(ns advent-of-code-2022.dayXX-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.dayXX :refer [part1 part2]]))

(deftest part1-test
  (testing "Part 1"
    (is (= "abc" (part1 "abc")))))

(deftest part2-test
  (testing "Part 2"
    (is (= "def" (part2 "def")))))
