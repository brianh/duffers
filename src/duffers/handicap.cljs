(ns duffers.handicap
  (:require
    [duffers.db :as db]))

(def golfer-scores
  {:mike [6 7 5 4 7 5 5 3 6 9 4 5 4 4 5 3 4 9]
   :chip [9 6 4 5 6 3 5 6 4 5 3 5 4 6 4 4 3 10]})

(def score-adjustments
  {74 {:handicap 0.5 :adjustment -2}
   75 {:handicap 0.5 :adjustment -1}
   76 {:handicap 0.5 :adjustment 0}

   77 {:handicap 1.0 :adjustment -2}
   78 {:handicap 1.0 :adjustment -1}
   79 {:handicap 1.0 :adjustment 0}

   80 {:handicap 1.5 :adjustment -2}
   81 {:handicap 1.5 :adjustment -1}
   82 {:handicap 1.5 :adjustment 0}
   83 {:handicap 1.5 :adjustment 1}

   84 {:handicap 2.0 :adjustment -2}
   85 {:handicap 2.0 :adjustment -1}
   86 {:handicap 2.0 :adjustment 0}
   87 {:handicap 2.0 :adjustment 1}

   88 {:handicap 2.5 :adjustment -2}
   89 {:handicap 2.5 :adjustment -1}
   90 {:handicap 2.5 :adjustment 0}
   91 {:handicap 2.5 :adjustment 1}

   92 {:handicap 3.0 :adjustment -2}
   93 {:handicap 3.0 :adjustment -1}
   94 {:handicap 3.0 :adjustment 0}
   95 {:handicap 3.0 :adjustment 1}

   96 {:handicap 3.5 :adjustment -2}
   97 {:handicap 3.5 :adjustment -1}
   98 {:handicap 3.5 :adjustment 0}
   99 {:handicap 3.5 :adjustment 1}

   100 {:handicap 4.0 :adjustment -2}
   101 {:handicap 4.0 :adjustment -1}
   102 {:handicap 4.0 :adjustment 0}
   103 {:handicap 4.0 :adjustment 1}
   104 {:handicap 4.0 :adjustment 2}

   105 {:handicap 4.5 :adjustment -2}
   106 {:handicap 4.5 :adjustment -1}
   107 {:handicap 4.5 :adjustment 0}
   108 {:handicap 4.5 :adjustment 1}
   109 {:handicap 4.5 :adjustment 2}

   110 {:handicap 5.0 :adjustment -2}
   111 {:handicap 5.0 :adjustment -1}
   112 {:handicap 5.0 :adjustment 0}
   113 {:handicap 5.0 :adjustment 1}
   114 {:handicap 5.0 :adjustment 2}})

(defn limit-to-double-par [holes score-card]
  (map #(min (* 2 %1) %2) holes score-card))

#_(defn calc-handi2 [golfer card total]
    (let [_ (println "golfer = " golfer)
          _ (println "card = " card)
          _ (println "total = " total)]))
;    {:keys [handicap adjustment]} (get data/score-adjustments total)]
;[handicap adjustment]))

(defn calc-handi [golfer card total]
  (let [{:keys [handicap adjustment]} (get score-adjustments total)
        whole-hole-cnt (Math/floor handicap)
        _ (println "whole-hole-cnt = " whole-hole-cnt)
        half-hole-cnt (if (> handicap whole-hole-cnt) 1 0)
        _ (println "half-hole-cnt = " half-hole-cnt)]
    [golfer (+ adjustment (reduce + (into (take whole-hole-cnt card) (take half-hole-cnt (drop whole-hole-cnt card)))))]))


(defn calc-handicaps [course]
  (for [[golfer card] golfer-scores]
    (let [holes (course db/courses)
          capped-score (limit-to-double-par holes card)
          sorted-card (reverse (sort capped-score))
          total (reduce + sorted-card)]
      (println sorted-card)
      (calc-handi golfer sorted-card total))))

