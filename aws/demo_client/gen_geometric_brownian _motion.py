import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from sklearn import preprocessing

def geometric_brownian_motion(T = 1, N = 100, mu = 0.1, sigma = 0.01, S0 = 20):
    dt = float(T)/N
    t = np.linspace(0, T, N)
    W = np.random.standard_normal(size = N)
    W = np.cumsum(W)*np.sqrt(dt) ### standard brownian motion ###
    X = (mu-0.5*sigma**2)*t + sigma*W
    S = S0*np.exp(X) ### geometric brownian motion ###
    return S


scale = 1000
N = 25
T = 0.5
S = 0.1
start = 1
y = pd.Series(geometric_brownian_motion(T, N, sigma=S, S0=start))
df = pd.DataFrame(y, columns=["Y"])
min_max_scaler = preprocessing.MinMaxScaler()
y_scaled = min_max_scaler.fit_transform(df.values) * scale
df = pd.DataFrame(y_scaled, columns=["Y"])
df.plot()
plt.show()

df.to_csv("last.csv", header=False)