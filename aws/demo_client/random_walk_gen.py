import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

# Probability to move up or down
prob = [0.8, 1]

# statically defining the starting position
start = 420
N = 15
scale = 15
positions = [start]

# creating the random points
rr = np.random.random(N)
downp = rr < prob[0]
upp = rr > prob[1]

for idownp, iupp in zip(downp, upp):
    down = idownp and positions[-1] > 1
    up = iupp and positions[-1] < 4
    positions.append(positions[-1] - down * scale + up * scale)

plt.plot(positions)
plt.show()

df = pd.DataFrame(positions)
df.to_csv("last.csv", header=False)