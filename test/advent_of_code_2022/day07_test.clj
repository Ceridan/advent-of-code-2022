(ns advent-of-code-2022.day07-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day07 :refer [part1 part2]]))

(def command-log-example [
                          "$ cd /"
                          "$ ls"
                          "dir a"
                          "14848514 b.txt"
                          "8504156 c.dat"
                          "dir d"
                          "$ cd a"
                          "$ ls"
                          "dir e"
                          "29116 f"
                          "2557 g"
                          "62596 h.lst"
                          "$ cd e"
                          "$ ls"
                          "584 i"
                          "$ cd .."
                          "$ cd .."
                          "$ cd d"
                          "$ ls"
                          "4060174 j"
                          "8033020 d.log"
                          "5626152 d.ext"
                          "7214296 k"])

(deftest part1-test
  (testing "Part 1"
    (is (= 95437 (part1 command-log-example)))))

(deftest part2-test
  (testing "Part 2"
    (is (= 24933642 (part2 command-log-example)))))
