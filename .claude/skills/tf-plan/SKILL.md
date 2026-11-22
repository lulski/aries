---
name: tf-plan
description: Run terraform fmt, validate and plan for a stack under terraform/. Use when the user asks to check, validate, or plan Terraform changes.
argument-hint: <stack-dir, e.g. backend-single-ec2>
allowed-tools: Bash(terraform:*), Read
---

# Terraform plan workflow

Target stack: `terraform/$ARGUMENTS` (default to `backend-single-ec2` if empty).
Available stacks: `backend`, `backend-single-ec2`, `s3`, `s3-img-bucket`.

1. `cd terraform/<stack>`
2. Run `terraform fmt -check -diff`. If it fails, show the diff and ask before formatting.
3. Run `terraform init -backend=false -input=false` then `terraform validate`.
4. Run `terraform plan -input=false -out=tfplan` and summarize:
   - resources added / changed / destroyed
   - anything being **destroyed or replaced**, flagged loudly
   If plan fails due to missing AWS credentials or backend config, report that and stop.
5. Never run `terraform apply`. Tell the user the command instead.
