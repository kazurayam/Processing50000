import uuid;
for i in range(50000):
    print str(i) + " " + uuid.uuid4().hex.upper()[0:6]
