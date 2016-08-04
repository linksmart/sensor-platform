CREATE TABLE HeartRateSample (
id INTEGER IDENTITY,
transmitted boolean NOT NULL,
timestamp VARCHAR(30) NOT NULL,
device VARCHAR(17) NOT NULL,
heartrate Integer NOT NULL,
energyexpended Integer NOT NULL,
rrintervals VARCHAR(30) NOT NULL
)
