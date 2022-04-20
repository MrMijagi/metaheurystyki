from sklearn.model_selection import ParameterGrid
from numpy import linspace
from itertools import count


tsRanges = [
    {
        "ITERATIONS": [4000],
        "N_SIZE": [20],
        "TABU_SIZE": [5, 10, 15, 20, 50, 100, 200, 500, 1000],
        "NEIGHBORS_TYPE": ["SWAP", "INVERSE"]
    },
    {
        "ITERATIONS": linspace(1000, 10000, 9),
        "N_SIZE": [5, 10, 15, 20, 40, 80, 160, 250],
        "TABU_SIZE": [20],
        "NEIGHBORS_TYPE": ["INVERSE"]
    }
]

saRanges = [
    {
        "ITERATIONS": [5000],
        "N_SIZE": linspace(100, 1000, 9),
        "T_START": [100],
        "T_END": [1],
        "T_METHOD": ["LINEAR", "SQUARE_ROOT"],
        "RANDOM_NEIGHBOR": ["SWAP", "INVERSE"]
    },
    {
        "ITERATIONS": [5000],
        "N_SIZE": [200],
        "T_START": [100, 200, 500, 1000, 2000, 5000, 10000],
        "T_END": [0.01, 0.05, 0.1, 0.5, 1, 5, 10, 100],
        "T_METHOD": ["SQUARE_ROOT"],
        "RANDOM_NEIGHBOR": ["INVERSE"]
    }
]

gaRanges = [
    {
        "POP_SIZE": [100],
        "EVOLUTIONS": [3000],
        "MUTATION_PROB": [0.3],
        "CROSS_PROB": [0.7],
        "MUTATION_TYPE": ["INVERSE"],
        "SELECTION_TYPE": ["TOURNAMENT"],
        "SELECTION_PARAM": [2, 5, 10, 15, 25, 50, 100],
        "CROSSOVER_TYPE": ["PMX"]
    },
    {
        "POP_SIZE": [100],
        "EVOLUTIONS": [3000],
        "MUTATION_PROB": [0.3],
        "CROSS_PROB": [0.7],
        "MUTATION_TYPE": ["INVERSE"],
        "SELECTION_TYPE": ["ROULETTE"],
        "SELECTION_PARAM": [1, 2, 3, 4, 5, 7, 10],
        "CROSSOVER_TYPE": ["PMX"]
    },
    {
        "POP_SIZE": [100],
        "EVOLUTIONS": [3000],
        "MUTATION_PROB": [0.3],
        "CROSS_PROB": [0.1, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.65, 0.75, 0.85],
        "MUTATION_TYPE": ["INVERSE"],
        "SELECTION_TYPE": ["TOURNAMENT"],
        "SELECTION_PARAM": [5],
        "CROSSOVER_TYPE": ["OX", "PMX"]
    },
    {
        "POP_SIZE": [100],
        "EVOLUTIONS": [3000],
        "MUTATION_PROB": [0.01, 0.05, 0.1, 0.2, 0.3, 0.4, 0.5, 0.25, 0.35, 0.15, 0.45],
        "CROSS_PROB": [0.7],
        "MUTATION_TYPE": ["INVERSE", "SWAP"],
        "SELECTION_TYPE": ["TOURNAMENT"],
        "SELECTION_PARAM": [5],
        "CROSSOVER_TYPE": ["OX", "PMX"]
    },
    {
        "POP_SIZE": [250, 500],
        "EVOLUTIONS": [1000],
        "MUTATION_PROB": [0.3],
        "CROSS_PROB": [0.7],
        "MUTATION_TYPE": ["INVERSE"],
        "SELECTION_TYPE": ["TOURNAMENT"],
        "SELECTION_PARAM": [5],
        "CROSSOVER_TYPE": ["OX", "PMX"]
    },
    {
        "POP_SIZE": [10, 20, 50, 100],
        "EVOLUTIONS": [3000],
        "MUTATION_PROB": [0.3],
        "CROSS_PROB": [0.7],
        "MUTATION_TYPE": ["INVERSE"],
        "SELECTION_TYPE": ["TOURNAMENT"],
        "SELECTION_PARAM": [5],
        "CROSSOVER_TYPE": ["OX", "PMX"]
    }
]


def generate(ranges, postfix):
    parameters = ParameterGrid(ranges)
    counters = count(start=0)

    for parameter, counter in zip(parameters, counters):
        save_combination(parameter, counter, postfix)


def save_combination(parameter, counter, postfix):
    with open(f"{counter:05d}.{postfix}.txt", 'w') as file:
        to_write = ""

        for key, value in parameter.items():
            to_write += f"{key} : {value}\n"

        file.write(to_write)


if __name__ == "__main__":
    for tsRange in tsRanges:
        generate(tsRange, "ts")

    for saRange in saRanges:
        generate(saRange, "sa")

    for gaRange in gaRanges:
        generate(gaRange, "ga")
