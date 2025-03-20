// import LoginForm from "@/components/LoginForm";

// export default function LoginPage() {
//   return <LoginForm></LoginForm>;
// }
"use client";
import {
  Button,
  Checkbox,
  Group,
  Paper,
  PasswordInput,
  TextInput,
} from "@mantine/core";
import { useForm } from "@mantine/form";

export default function Demo() {
  const form = useForm({
    mode: "uncontrolled",
    initialValues: {
      email: "",
      termsOfService: false,
    },

    validate: {
      email: (value) => (/^\S+@\S+$/.test(value) ? null : "Invalid email"),
    },
  });

  return (
    <Paper shadow="md" p={"lg"}>
      <form onSubmit={form.onSubmit((values) => console.log(values))}>
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
        ></PasswordInput>
        <Group justify="flex-end" mt="md">
          <Button type="submit">Submit</Button>
        </Group>
      </form>
    </Paper>
  );
}
