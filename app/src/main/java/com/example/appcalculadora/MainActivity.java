package com.example.appcalculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        txtResultado = (TextView) findViewById(R.id.txtResultado);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void pressButton(View view){
        // 1. Cast da View para Button (para podermos ler o texto)
        Button botaoClicado = (Button) view;

        // 2. Captura o texto do botão (ex: "7", "8", "+")
        String textoBotao = botaoClicado.getText().toString();

        // Normalização: Se o botão for "X", tratamos como "*" para a exp4j entender depois
        if(textoBotao.equals("X")){
            textoBotao = "*";
        }

        // 3. Captura o que já está escrito no TextView
        String textoAtualVisor = txtResultado.getText().toString();

        // Se o visor mostrar "0" ou qualquer mensagem de "Erro",
        // nós substituímos o texto completamente em vez de apenas somar.
        if (textoAtualVisor.equals("0") || textoAtualVisor.contains("Erro")) {
            txtResultado.setText(textoBotao);
        } else {
            // Caso contrário, continuamos concatenando a expressão
            txtResultado.setText(textoAtualVisor + textoBotao);
        }
    }

    public void calcular(View view){
        String expressaoNoVisor = txtResultado.getText().toString();

        // Evita processar se o visor estiver vazio ou já estiver exibindo erro
        if (expressaoNoVisor.isEmpty() || expressaoNoVisor.contains("Erro")) return;

        try {
            // Substituímos o 'X' por '*' caso você esteja usando 'X' no visor
            // A exp4j só entende '*' para multiplicação
            String expressaoFormatada = expressaoNoVisor.replace("X", "*");

            Expression e = new ExpressionBuilder(expressaoFormatada).build();
            double resultado = e.evaluate();

            txtResultado.setText(String.valueOf(resultado));


        } catch (Exception e) {
            // Se der erro de sintaxe, limpamos e avisamos
            txtResultado.setText("0");
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void clear(View view){
        txtResultado.setText("0");
    }
}