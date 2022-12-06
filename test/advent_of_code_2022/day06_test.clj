(ns advent-of-code-2022.day06-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day06 :refer [part1 part2]]))

(deftest part1-test
  (testing "Part 1"
    (is (= 7 (part1 "mjqjpqmgbljsphdztnvjfqwrcgsmlb")))
    (is (= 5 (part1 "bvwbjplbgvbhsrlpgdmjqwftvncz")))
    (is (= 6 (part1 "nppdvjthqldpwncqszvftbrmjlhg")))
    (is (= 10 (part1 "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")))
    (is (= 11 (part1 "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")))))

(deftest part2-test
  (testing "Part 2"
    (is (= 19 (part2 "mjqjpqmgbljsphdztnvjfqwrcgsmlb")))
    (is (= 23 (part2 "bvwbjplbgvbhsrlpgdmjqwftvncz")))
    (is (= 23 (part2 "nppdvjthqldpwncqszvftbrmjlhg")))
    (is (= 29 (part2 "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")))
    (is (= 26 (part2 "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")))))
