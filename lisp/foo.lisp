(defun foo (n)
    (lambda (i) (incf n i)))

(format t "~a~%" (foo 2))
;(format t "~a~%" ( (foo 2) 2 ))
(setq a nil)
(push 4 a)
(push 5 a)
(format t "~a~%" (pop a))
a
(format t "~d~o~b~9~%" 100 100 100 100)
(format t "~a~a~a~a~%" 100 100 100 100)
(format *query-io* "output message")
(force-output *query-io*)
(format t "~a~%" "output message")
(format t "~a~%" "output message")
(format t "~%")
(format t "~a:!`0t~a~%":author "berlinix")
(format t "~a~%" "helloworld")
(format t "~a~a~%" "helloworld" 100)
(format t "~a~%" '(1 2))
(format t "~a~%" (first '(1 2 3)))
(format t "~a~%" (rest '(1 2 3)))
(format t "~a~%" (cons '4 '(1 2 3)))
(format t "~a~%" (list '4 '(1 2 3)))
(format t "~a~%" (append '(4) '(1 2 3)))

(defun my_second (lst)
    (first (rest lst))
)
(format t "~a~%" (my_second '(1 2 3)))

(defun my_max (x y)
    (if (> x y) x y)
)

(format t "~a~%" (my_max 3 5))
(defun total (x)
    (if (null x)
    0
    (+ (first x) (total (rest x)))
    )
)

(format t "~a~%" (total '(3 5 8)))
(setf total2 '(lambda (a b) (+ a b)))
(defun total3 (a b) (+ a b))
;(format t "~a~%" (setf total2 '(lambda (a b) (+ a b))))
(format t "~a~%" (total3 5 8))
(format t "~a~%" total2)
(apply (lambda (a b) (+ a b)) '(101 102))
(defmacro time_two (x) (* 2 x))
(setf a 4)
;(time_two a)
(format t "~a~%" (time_two 4))
(apply (lambda (a b) (+ a b)) '(101 102))
(apply (lambda (a b) (+ a b)) '(101 102))
(apply (lambda (a b) (+ a b)) '(101 102))
(apply (lambda (a b) (+ a b)) '(101 102))
(format t "~a~%" (time_two 4))
