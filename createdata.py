import uuid;

print "seq,data";

for i in range(50000):
    print str(i+1) + "," + uuid.uuid4().hex.upper()[0:6]
