import ast
file = open('top_cat.txt', 'r')
buffer = file.read()
ret_dict = ast.literal_eval(buffer)
print str(len(ret_dict['itemcats_get_response']['item_cats']['item_cat']))
