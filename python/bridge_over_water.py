



PEOPLE ={
            "A" : 1,
            "B" : 2,
            "C" : 5,
            "D" : 10
        }


class BridgeOverWater():


    def main(self):

        sorted_pair_sequence = self.__get_sorted_pair_sequence()

        pair_formula = self.__get_pair_formula(sorted_pair_sequence)
        print("pair_formula: ", pair_formula)

        int_formula = self.__get_int_formula(pair_formula)
        int_formula_str = "+".join([str(int) for int in int_formula])
        print("int_formula_str: ", int_formula_str)

        cross_time = sum(int_formula)
        print("cross_time: ", cross_time)



    def __get_sorted_pair_sequence(self):
        """
        Here we calculate a clean sorted pair sequence that will be used as a blueprint in building the
        bridge crossing formulas.
        :return: fastest_cntnr
        """
        pairs_cntnr = PEOPLE.copy()
        fastest_cntnr = []
        slowest_cntnr = []

        while len(pairs_cntnr) > 0:
            all_pairs = self.__get_all_pairs(pairs_cntnr)
            pair_sums = self.__get_pair_sums(all_pairs)

            fastest_pair = pair_sums[0].keys()[0]
            fastest_cntnr.append(fastest_pair)

            slowest_pair = pair_sums[len(pair_sums) - 1].keys()[0]
            slowest_subcntnr = [slowest_pair]
            slowest_subcntnr.extend(slowest_cntnr)
            slowest_cntnr = slowest_subcntnr

            pairs_cntnr = self.__get_leftover_pairs(pairs_cntnr, fastest_pair, slowest_pair)

        fastest_cntnr.extend(slowest_cntnr)

        return fastest_cntnr



    def __get_int_formula(self, pair_formula):
        """
        Here we convert strings in pair formula into integers, highest between pair integers
        and built a formula using integers that we then can sum to calculate total time it
        took to cross the bridge.
        :param pair_formula:
        :return: int_formula
        """
        int_formula = []
        for element in pair_formula:
            singles = element.split("|")
            if len(singles) > 1:
                int_formula.append(max([PEOPLE[singles[0]], PEOPLE[singles[1]]]))
            else:
                int_formula.append(PEOPLE[element])

        return int_formula



    def __get_pair_formula(self, pairs):
        """
        Here by analyzing sorted_pair_sequence we derive the formula against,
        which we will use as blueprint to calculate time it takes to cross the bridge.
        :param sorted_pair_sequence:
        :return: pair_formula
        """
        fastest_pair = pairs[0]
        pairs.remove(fastest_pair)
        fastest_pair_list = fastest_pair.split("|")

        pair_formula = []
        other_side = []
        pairs_counter = 0
        exit_loop = False
        while not exit_loop:
            if len(other_side) == 0:
                pair_formula.append(fastest_pair)
                other_side.extend([fastest_pair_list[0], fastest_pair_list[1]])
                pairs_counter -= 1

            elif len(other_side) == 2:
                pair_formula.append(fastest_pair_list[0])
                pair_formula.append(pairs[pairs_counter])
                other_side.remove(fastest_pair_list[0])

            else:
                pair_formula.append(fastest_pair_list[1])
                other_side.remove(other_side[0])
                pairs_counter -= 1

            if pairs_counter >= len(pairs)-1:
                if len(other_side) < 2:
                    pair_formula.append(other_side[0])
                pair_formula.append(fastest_pair)
                exit_loop = True

            pairs_counter += 1

        return pair_formula




    def __get_leftover_pairs(self, pairs, fastest_pair, slowest_pair):

        """
        Here we filter out the pairs that we have already stored away in previous in current itteration,
        and we return a list of pairs, which we haven't yet analyzed.
        :param pairs_cntnr:
        :param fastest_pair:
        :param slowest_pair:
        :return: pairs
        """
        for person in fastest_pair.split("|"):
            try:
                pairs.pop(person)
            except:
                pass

        for person in slowest_pair.split("|"):
            try:
                pairs.pop(person)
            except:
                pass

        return pairs



    def __get_pair_sums(self, all_pairs):
        """
        In this method we are summarizing combined time for all pairs.
        At the same time we are sorting the results so we could further know what pars are slowest
        and what pairs are fastest and what pairs are slower or faster than other pairs.
        :param all_pairs:
        :return: pair_sums
        """
        sums = []
        pair_sums = []
        strg = {}

        for pair in all_pairs:
            pair_split = pair.split("|")
            pair_sum = PEOPLE[pair_split[0]] + PEOPLE[pair_split[1]]
            sums.append(pair_sum)
            strg[str(pair_sum)] = pair

        for sum in sorted(sums):
            pair_sums.append({strg[str(sum)]:sum})

        return pair_sums



    def __get_all_pairs(self, pairs_cntnr):
        """
        In this method we are permutating to get a list of all possible pairs from the
        given list given as an input.
        :return: all_pairs
        """
        all_pairs = []

        for key_one in pairs_cntnr:
            for key_two in pairs_cntnr:
                pair = key_one+"|"+key_two
                if not pair == key_one+"|"+key_one \
                        and not key_two+"|"+key_one in all_pairs:
                    all_pairs.append(pair)

        return all_pairs




if __name__ == '__main__':
    run = BridgeOverWater()
    run.main()

