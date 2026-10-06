import random
def generatePack(n_players, max):
    cards = [str(random.randint(1,max)) for _ in range(8*n_players)]
    with open("pack1.txt", "w") as f:
        for n in cards:
            f.writelines(n + "\n")
        f.close()

generatePack(4, 100)