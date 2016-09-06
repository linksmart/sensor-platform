CREATE TABLE HeartRateSample (
id INTEGER IDENTITY,
transmitted boolean NOT NULL,
timestamp VARCHAR(30) NOT NULL,
device VARCHAR(17) NOT NULL,
heartrate Integer NOT NULL,
energyexpended Integer NOT NULL,
rrintervals VARCHAR(100) NOT NULL
)

CREATE TABLE PulseOximeterSample (
id INTEGER IDENTITY,
timestamp VARCHAR(30) NOT NULL,
device VARCHAR(17) NOT NULL,
spo2 INTEGER NOT NULL,
pulserate INTEGER NOT NULL
)

CREATE TABLE CC2650TemperatureSample (
id INTEGER IDENTITY,
timestamp VARCHAR(30) NOT NULL,
device VARCHAR(17) NOT NULL,
objecttemperature FLOAT NOT NULL,
dietemperature FLOAT NOT NULL
)

CREATE TABLE CC2650PressureSample (
id INTEGER IDENTITY,
timestamp VARCHAR(30) NOT NULL,
device VARCHAR(17) NOT NULL,
temperature FLOAT NOT NULL,
pressure FLOAT NOT NULL
)

CREATE TABLE CC2650HumiditySample (
id INTEGER IDENTITY,
timestamp VARCHAR(30) NOT NULL,
device VARCHAR(17) NOT NULL,
temperature FLOAT NOT NULL,
humidity FLOAT NOT NULL
)

CREATE TABLE CC2650AmbientlightSample (
id INTEGER IDENTITY,
timestamp VARCHAR(30) NOT NULL,
device VARCHAR(17) NOT NULL,
ambientlight FLOAT NOT NULL
)

CREATE TABLE CC2650MovementSample (
id INTEGER IDENTITY,
timestamp VARCHAR(30) NOT NULL,
device VARCHAR(17) NOT NULL,
rotationX FLOAT,
rotationY FLOAT,
rotationZ FLOAT,
accelerationX FLOAT,
accelerationY FLOAT,
accelerationZ FLOAT,
magnetismX FLOAT,
magnetismY FLOAT,
magnetismZ FLOAT
)