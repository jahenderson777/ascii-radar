(ns ascii-radar.core-test
  (:require [clojure.test :refer [deftest is run-tests testing]]
            [ascii-radar.core :as core]
            [clojure.string :as str]))

(def invader-1
  "--o-----o--
---o---o---
--ooooooo--
-oo-ooo-oo-
ooooooooooo
o-ooooooo-o
o-o-----o-o
---oo-oo---")

(def invader-2
  "
---oo---
--oooo--
-oooooo-
oo-oo-oo
oooooooo
--o--o--
-o-oo-o-
o-o--o-o")



(deftest test-score 
  (testing "testing that score gives 1.0 when both inputs are equal"
    (is (= 1.0
           (core/score (apply str (str/split-lines (str/trim invader-1)))
                       (apply str (str/split-lines (str/trim invader-1))))))
    (is (= 1.0
           (core/score (apply str (str/split-lines (str/trim invader-2)))
                       (apply str (str/split-lines (str/trim invader-2)))))))
  
  (testing "changing just one location at the end reduces the score appropriately"
    (is (= (float 0.9886364)
           (core/score (apply str (str/split-lines (str/trim invader-1)))
            (apply str (str/split-lines (str/trim "--o-----o--
---o---o---
--ooooooo--
-oo-ooo-oo-
ooooooooooo
o-ooooooo-o
o-o-----o-o
---oo-oo--o")))))))
  
  (testing "changing just one location at the beginning reduces the score appropriately"
    (is (= (float 0.9886364)
           (core/score (apply str (str/split-lines (str/trim invader-1)))
            (apply str (str/split-lines (str/trim "--o-----o--
o--o---o---
--ooooooo--
-oo-ooo-oo-
ooooooooooo
o-ooooooo-o
o-o-----o-o
---oo-oo---"))))))))


(run-tests)