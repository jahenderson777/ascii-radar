(ns ascii-radar.core
  (:require [clojure.string :as str])
  
  (:gen-class))



(def known-invaders
  ["
--o-----o--
---o---o---
--ooooooo--
-oo-ooo-oo-
ooooooooooo
o-ooooooo-o
o-o-----o-o
---oo-oo---"
   
   "
---oo---
--oooo--
-oooooo-
oo-oo-oo
oooooooo
--o--o--
-o-oo-o-
o-o--o-o"
   ])

(def radar 
  "----o--oo----o--ooo--ooo--o------o---oo-o----oo---o--o---------o----o------o-------------o--o--o--o-
--o-o-----oooooooo-oooooo---o---o----o------ooo-o---o--o----o------o--o---ooo-----o--oo-o------o----
--o--------oo-ooo-oo-oo-oo-----O------------ooooo-----oo----o------o---o--o--o-o-o------o----o-o-o--
-------o--oooooo--o-oo-o--o-o-----oo--o-o-oo--o-oo-oo-o--------o-----o------o-ooooo---o--o--o-------
------o---o-ooo-ooo----o-----oo-------o---oo-ooooo-o------o----o--------o-oo--ooo-oo-------------o-o
-o--o-----o-o---o-ooooo-o-------oo---o---------o-----o-oo-----------oo----ooooooo-ooo-oo------------
o-------------ooooo-o--o--o--o-------o--o-oo-oo-o-o-o----oo------------o--oooo--ooo-o----o-----o--o-
--o-------------------------oo---------oo-o-o--ooo----oo----o--o--o----o--o-o-----o-o------o-o------
-------------------o----------o------o--o------o--------o--------o--oo-o-----oo-oo---o--o---o-----oo
----------o----------o---o--------------o--o----o--o-o------------oo------o--o-o---o-----o----------
------o----o-o---o-----o-o---o-----oo-o--------o---------------------------------o-o-o--o-----------
---------------o-------o-----o-------o-------------------o-----o---------o-o-------------o-------oo-
-o--o-------------o-o-----o--o--o--oo-------------o----ooo----o-------------o----------oo----o---o-o
-o--o-------------o----oo------o--o-------o--o-----o-----o----o-----o--o----o--oo-----------o-------
-o-----oo-------o------o----o----------o--o----o-----o-----o-------o-----------o---o-o--oooooo-----o
-o--------o-----o-----o---------oo----oo---o-o---------o---o--oooo-oo--o-------o------oo--oo--o-----
------------o---------o---------o----oooo-------------oo-oo-----ooo-oo-----o-------o-oo-oooooooo---o
----------------------o------------oooooooo---o-----o-------o--oooooo-o------------o-o-ooooooo-o----
------------o------o---o---o-------oo-oo--o--o---------o--o-o-o-ooooo-o--------------oo-o----o-oo-o-
---o-o----------oo-------oo----o----oooooooo-------o----o-o-o-o-----o-o-----o----------ooo-oo--o---o
-o-o---------o-o---------------o--o--o--ooo---ooo-------o------oo-oo------------o--------o--o-o--o--
-------oo---------------------------o-oo----------o------o-o-------o-----o----o-----o-oo-o-----o---o
---o--------o-----o-------o-oo-----oo--oo-o----oo----------o--o---oo------oo----o-----o-------o-----
---o--ooo-o---------o-o----o------------o---------o----o--o-------o----o--------o----------------oo-
---o------o----------------o----o------o------o---oo-----------o-------------o----------oo---------o
--oo---------------o--o------o---o-----o--o-------------o------o-------o-----o-----o----o------o--o-
-o-------o----------o-o-o-------o-----o--o-o-----------o-oo-----------o------o---------o-----o-o----
----------o----o-------o----o--o------o------------o---o---------------oo----o-----ooo--------------
----o--------oo----o-o----o--o------ooo----o-oooo---o--o-oo--------o-oo-----o-o---o-o--o-----oo-----
------o--------o-ooooo----o---o--o-----o---------------o-o-------o-----o----------------------------
o-------oo----o--oooooo-o---o--o------oooo----------o-oo-------o---o----------o------oo-------------
-o---o----------o--oo-oo-o---o-----o-o-----------------------oo--o------o------o--------------------
-----oo-o-o-o---ooooooooo----o----o--------o--o---oo---o------------o----------o-o---o------o-o--oo-
------o------o---ooo-o---------------------------o--o---o---o----o--o-------o-----o------o----o----o
-------o----------ooo-o-----o----o---o--o-oo--o--o-o--o------o--o-oo---ooo------------------------o-
-o-------o------o-o--ooo--o---o---oo-----o----o-------------o----o-ooo-o------o--o-o------o-o-------
---oo--o---o-o---------o---o--------------o--o-----o-------o-----o--o---o-oo--------o----o----o-----
o------o----oo-o-----------oo--o---o--------o-o------o-------o-o------o-oo---------o-----oo---------
----o--o---o-o-----------o---o------------o-------o----o--o--o--o-o---------------o-----------------
-------oo--o-o-----o-----o----o-o--o----------------------o-------o------o----oo----ooo---------o---
o-----oo-------------------o--o-----o-----------o------o-------o----o-----------o----------------o--
--o---o-------o------------o--------------------o----o--o-------------oo---o---------oo--------o----
--o--------o---------o------------o------o-------o------------o-------o---o---------ooooo-----------
------o--------------o-o-o---------o---o-------o--o-----o-------o-o----------o-----oo-ooo----------o
--o---------------o----o--oo-------------o---------o-------------------oo---------oo-o-ooo----------
-o-----------o------ooo----o----------------ooo-----o--------o--o---o-----------o-o-oooooo--------oo
-o---o-------o---o-oooo-----o-------------------o----oo-----------------o--o--------o--o------o--o--
-------o---o------oooooo--o----ooo--o--------o-------o----------------------------oo-oo-o--o--------
o--oo------o-----oo--o-oo------------oo--o------o--o-------------oo----o------------oooo-o------oo--
-----o----------ooooooooo--------------oo--------------oo-----o-----o-o--o------o----------o----o---")

;; Your Clojure application must take a radar sample as an argument and reveal possible locations of those pesky invaders.


;; so I guess we need to scan over a given radar sample, shifting a 'window' around the same size of each invader,
;; seeing how closely that 'window' matches a known invader, generating like a score for how closely we get a match.

;; we'll do the scanning over the whole sample later, let's first tackle being given a window that is the same size
;; as our known invader.

;; other thoughts: when dealing with the edges we might want to have a third state of a grid position being 'unknown',
;; meaning this is the outside of the radar.

;; also, we might want to give more weight to bits of the window that are closer to the center, but perhaps lets 
;; ignore that for now


;; maybe there's some maths trick to very quickly score a sample by like converting it to a binary number,
;; but lets just do it simply for now


;; we can probably forget about new lines in the sample, and just consider it a long string.

(defn gen-test-sample [width height])


(defn score [a b]
  (let [paired (map vector a b)
        s (reduce (fn [score [ax bx]]
                    (if (or (= ax \U)
                            (= bx \U))
                      score ;; we have an Unknown location, e.g. beyond the edge
                      (if (= ax bx)
                        (+ score 1) ;; we have a match
                        score ;; we have no match
                        )))
                  0 paired)]
    (float (/ s (count a)))))

(score (nth known-invaders 0)
       "
                    --------o--
                    ---o---o---
                    --ooooooo--
                    -oo-ooo-oo-
                    ooooooooooo
                    o-ooooooo-o
                    o-o-----o-o
                    ---oo-oo-oo"
       )



;; ok scoring two samples is basically working, now for windowing over a larger radar sample
;; need a function that takes a big radar sample, a width and a height, and an x & y position, and gives the sub-sample

(defn add-unknowns [sample w h]
  (let [lines (str/split-lines sample)
        middle (map (fn [l]
                      (let [u (apply str (repeat w "U"))]
                        (str u l u)))
                    lines)
        start-end (repeat h (apply str (repeat (+ (count (first lines)) (* 2 w)) 
                                               "U")))]
    (vec (concat start-end middle start-end)))
  )


(add-unknowns radar 6 10)

(defn sub-sample [sample w h x y]
  (let [sample' (add-unknowns sample w h)
        lines (subvec sample' y (+ y h))]
    (map (fn [l] (subs l x (+ x w))) lines)))

;; now we need a seq of x,y positions to map over

(defn all-locations [sample]
  (let [lines (str/split-lines sample)
        sample-w (count (first lines))
        sample-h (count lines)]
    (for [y (range sample-h)
          x (range sample-w)]
      [x y])))

;; maybe reduce over all-locatins and only collect the ones above a threshold

(let [invader (str/trim (nth known-invaders 0))
      invader-lines (str/split-lines invader)
      invader-w (count (first invader-lines))
      invader-h (count invader-lines)]
  (reduce (fn [v [x y]]
            (let [samp (sub-sample radar invader-w invader-h x y)
                  s (score (apply str samp) (apply str invader-lines))]
              (if (> s 0.7)
                (conj v [[x y] s])
                v)))
          []
          (all-locations radar)))

(defn scan-for-invader [radar invader threshold]
  (let [invader (str/trim invader)
        invader-lines (str/split-lines invader)
        invader-w (count (first invader-lines))
        invader-h (count invader-lines)]
    (reduce (fn [v [x y]]
              (let [samp (sub-sample radar invader-w invader-h x y)
                    s (score (apply str samp) (apply str invader-lines))]
                (if (> s threshold)
                  (do (println "match, invader" s)
                      (println invader)
                      (println "samp")
                      (println (str/join "\n" samp))
                      (conj v [[x y] s]))
                  v)))
            []
            (all-locations radar))))


(map (fn [invader]
       (scan-for-invader radar invader 0.75))
     known-invaders)


(defn -main [& args]
  )

