"use client";
import {
  Button,
  Group,
  Paper,
  PasswordInput,
  TextInput,
  Alert,
} from "@mantine/core";
import { useState } from "react";
import { useForm } from "@mantine/form";
import { IconCheck, IconX } from "@tabler/icons-react";
import { useRouter, useSearchParams } from "next/navigation";

export default function LoginPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const returnUrl = searchParams.get("returnUrl");
  
  const mustNotBeEmpty = (value: string) =>
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

  type AuthState = "idle" | "success" | "failed";

  const [authState, setAuthState] = useState<AuthState>("idle");
  const [authResponseLabel, setAuthResponseLabel] = useState<string[]>([]);

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
        setAuthState("success");
        router.push(returnUrl ? decodeURIComponent(returnUrl) : "/");
      } else {
        console.info("Login failed:", data.message);
        setAuthState("failed");
        setAuthResponseLabel(data.message);
      }
    } catch (err) {
      console.error("Error during login:", err);
    }
  };

  //client validation
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

      {authState === "success" && (
        <Alert
          mt="md"
          color="green"
          icon={<IconCheck size={16} />}
          title="Success"
        >
          You have been logged in!
        </Alert>
      )}

      {authState === "failed" && (
        <Alert
          mt="md"
          color="red"
          icon={<IconX size={16} />}
          title="Authentication Failed"
        >
          <ul>
            {authResponseLabel?.map((msg, i) => (
              <p key={i}>{msg}</p>
            ))}
          </ul>
        </Alert>
      )}
    </Paper>
  );
}
