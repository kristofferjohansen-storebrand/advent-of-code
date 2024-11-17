package no.krazyglitch.aoc2015.day15;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class MilkDunking {

    public MilkDunking() {
        try {
            final List<String> data = FileUtil.readInputFile(this.getClass());
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The highest score possible is %d\n", calculateWinningCombination(data, 100));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The highest score possible with 500 calories is %d\n", calculateWinningCombinationWithCalories(data, 100, 500));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static int calculateWinningCombination(final List<String> data, final int maximumIngredients) {
        final List<Ingredient> ingredients = parseIngredients(data);
        final List<Map<Ingredient, Integer>> combinations = getCombinations(ingredients, maximumIngredients);

        return combinations.stream()
                .mapToInt(MilkDunking::calculateScore)
                .max()
                .orElseThrow(() -> new RuntimeException("Could not find winning combination"));
    }

    public static int calculateWinningCombinationWithCalories(final List<String> data, final int maximumIngredients, final int calorieCount) {
        final List<Ingredient> ingredients = parseIngredients(data);
        final List<Map<Ingredient, Integer>> combinations = getCombinations(ingredients, maximumIngredients);

        return combinations.stream()
                .filter(cookie -> getCalorieCount(cookie) == 500)
                .mapToInt(MilkDunking::calculateScore)
                .max()
                .orElseThrow(() -> new RuntimeException("Could not find winning combination with " + calorieCount + " calories"));
    }

    private static List<Map<Ingredient, Integer>> getCombinations(final List<Ingredient> ingredients, final int maximumIngredients) {
        final List<Map<Ingredient, Integer>> combinations = new ArrayList<>();
        generateCombinations(new HashMap<>(), maximumIngredients, 0, ingredients, combinations);

        return combinations;
    }

    private static void generateCombinations(final Map<Ingredient, Integer> currentCombination,
                                             final int remainingIngredients,
                                             final int ingredientIndex,
                                             final List<Ingredient> ingredients,
                                             final List<Map<Ingredient, Integer>> combinations) {
        if (remainingIngredients == 0) {
            combinations.add(new HashMap<>(currentCombination));
            return;
        }

        for (int i = ingredientIndex; i < ingredients.size(); i++) {
            final Ingredient ingredient = ingredients.get(i);
            currentCombination.put(ingredient, currentCombination.getOrDefault(ingredient, 0) + 1);
            generateCombinations(currentCombination, remainingIngredients - 1, i, ingredients, combinations);
            currentCombination.put(ingredient, currentCombination.get(ingredient) - 1);
        }
    }

    private static int calculateScore(final Map<Ingredient, Integer> cookie) {
        int capacity = 0;
        int durability = 0;
        int flavor = 0;
        int texture = 0;

        for (final Map.Entry<Ingredient, Integer> entry : cookie.entrySet()) {
            final Ingredient ingredient = entry.getKey();
            final int amount = entry.getValue();

            capacity += ingredient.getCapacity(amount);
            durability += ingredient.getDurability(amount);
            flavor += ingredient.getFlavor(amount);
            texture += ingredient.getTexture(amount);
        }

        if (capacity <= 0 || durability <= 0 || flavor <= 0 || texture <= 0) {
            return 0;
        }

        return capacity * durability * flavor * texture;
    }

    private static int getCalorieCount(final Map<Ingredient, Integer> cookie) {
        return cookie.entrySet().stream()
                .mapToInt(ingredient -> ingredient.getKey().getCalories(ingredient.getValue()))
                .sum();
    }

    private static List<Ingredient> parseIngredients(final List<String> data) {
        return data.stream()
                .map(Ingredient::new)
                .toList();
    }

    public static void main(final String[] args) {
        new MilkDunking();
    }
}

class Ingredient {
    private static final Pattern COOKIE_PATTERN = Pattern.compile("(\\w+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (\\d+)");

    private final String name;
    private final int capacity;
    private final int durability;
    private final int flavor;
    private final int texture;
    private final int calories;

    public Ingredient(final String data) {
        final Matcher matcher = COOKIE_PATTERN.matcher(data);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Ingredient could not be parsed from: " + data);
        }

        this.name = matcher.group(1);
        this.capacity = Integer.parseInt(matcher.group(2));
        this.durability = Integer.parseInt(matcher.group(3));
        this.flavor = Integer.parseInt(matcher.group(4));
        this.texture = Integer.parseInt(matcher.group(5));
        this.calories = Integer.parseInt(matcher.group(6));
    }

    public int getCapacity(final int multiplier) {
        return capacity * multiplier;
    }

    public int getDurability(final int multiplier) {
        return durability * multiplier;
    }

    public int getFlavor(final int multiplier) {
        return flavor * multiplier;
    }

    public int getTexture(final int multiplier) {
        return texture * multiplier;
    }

    public int getCalories(final int multiplier) {
        return calories * multiplier;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Ingredient that = (Ingredient) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", capacity=" + capacity +
                ", durability=" + durability +
                ", flavor=" + flavor +
                ", texture=" + texture +
                ", calories=" + calories +
                '}';
    }
}