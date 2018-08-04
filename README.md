# minil
A tiny interpreter for learning how to implement programming language.<br>
Implemented using [antlr4](http://www.antlr.org/).

## Syntax
The syntax is similar to major scripting languages like ruby, or python.
```ruby
def factorial(i)
    if i == 1
        return 1
    else
        return i * factorial(i - 1)
    end
end

v = factorial(6)
print(v)   # 720
```
```ruby
def bubblesort(data)
    loop = 0
    while loop < data.length do
        ri = data.length - 1
        li = data.length - 2

        while li >= loop do
            r = data[ri]
            l = data[li]

            if r < l
                swap(data, ri, li)
            end
            ri = ri - 1
            li = li - 1
        end
        loop = loop + 1
    end
end

def swap(arr, xi, yi)
    tmp = arr[xi]
    arr[xi] = arr[yi]
    arr[yi] = tmp
end


data = [1,9,2,7,4]
bubblesort(data)
print(data)   # [1,2,4,7,9]
```
