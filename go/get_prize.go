package main



import (

    "math/rand"

    "fmt"

    "time"

    "sort"

)



func main() {

    candi_count := 15

    all_candi := 51

    comp_count := 3

    for c := comp_count; c >= 0; c-- {

        if c > 0 {

            fmt.Printf("Countdown %d...\n", c)

        }

        r := rand.New(rand.NewSource(time.Now().UnixNano()))

        result := make(map[int]int)

        for ; len(result) < candi_count; {

            e := r.Intn(all_candi) + 1

            result[e] = 0

        }

        if c == 0 {

            res := make([]int, candi_count)

            i := 0

            for k, _ := range result {

                res[i] = k

                i++

            }

            sort.Ints(res)

            fmt.Println(res)

        }

    }


}