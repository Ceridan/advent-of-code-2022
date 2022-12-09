(ns advent-of-code-2022.day09-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day09 :refer [part1 part2]]))

(def small-instructions-example ["R 4"
                                 "U 4"
                                 "L 3"
                                 "D 1"
                                 "R 4"
                                 "D 1"
                                 "L 5"
                                 "R 2"])

(def large-instructions-example ["R 5"
                                 "U 8"
                                 "L 8"
                                 "D 3"
                                 "R 17"
                                 "D 10"
                                 "L 25"
                                 "U 20"])

(deftest part1-test
  (testing "Part 1"
    (is (= 13 (part1 small-instructions-example)))))

(deftest part2-test
  (testing "Part 2"
    (is (= 1 (part2 small-instructions-example)))
    (is (= 36 (part2 large-instructions-example)))))
