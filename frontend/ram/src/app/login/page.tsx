"use client";
import { Button, Group, Paper, PasswordInput, TextInput } from "@mantine/core";
import { useState } from "react";
import { useForm } from "@mantine/form";

export default function LoginPage() {
  const mustNotBeEmpty = (value) =>
    value.trim() === "" ? "Must be filled" : null;

  const form = useForm({
    mode: "uncontrolled",
    initialValues: {
      username: "",
      password: "",
    },
    validate: {
      username: mustNotBeEmpty,
      password: mustNotBeEmpty,
    },
  });

  const handleSubmit = async (values: typeof form.values) => {
    console.log(values);
    try {
      const res = await fetch("/api/authenticate", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(values),
      });

      const data = await res.json();

      if (data.success) {
        console.log("Logged in successfully:", data);
        // handle redirect or session setup here
      } else {
        console.error("Login failed:", data.message);
      }
    } catch (err) {
      console.error("Error during login:", err);
    }
  };

  const [errorLabel, setErrorLabel] = useState(null);

  const handleError = (validationErrors, values, event) => {
    console.log(validationErrors, values, event);
    if (validationErrors.password !== null) {
      setErrorLabel(validationErrors.password);
    }
  };

  return (
    <Paper shadow="md" p={"lg"}>
      <form onSubmit={form.onSubmit(handleSubmit, handleError)}>
        <TextInput
          withAsterisk
          label="Username:"
          placeholder=""
          key={form.key("username")}
          {...form.getInputProps("username")}
        />

        <PasswordInput
          withAsterisk
          label="Password:"
          key={form.key("password")}
          error={errorLabel}
          {...form.getInputProps("password")}
        ></PasswordInput>
        <Group justify="flex-end" mt="md">
          <Button type="submit">Submit</Button>
        </Group>
      </form>
    </Paper>
  );
}
