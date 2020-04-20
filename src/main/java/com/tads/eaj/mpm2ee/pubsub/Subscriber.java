/*
 * Objeto que representa um assinante da rede. 
 * Tem a função declarar interesse sobre um tópico específico e receber
 * as mewnsagens publicadas no tópico de interesse.
 * O método callback da classe SimpleCallback ouve a rede e sempre que ouver 
 * um novo dado publicado neste tópico o mosquitto notifica e o subscriber 
 * recebe a mensagem.
 */
package com.tads.eaj.mpm2ee.pubsub;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author Lucas Bernardo
 */
public class Subscriber {

    public static void main(String[] args) {

        String[] topics = {"orion/energyPolicy", "orion/temperature"};
        String content = "level 4";
        int[] qos = {2, 2};
        String broker = "tcp://mqtt.eclipse.org:1883";
        String clientId = "sub_NodeESP-1_23434";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient cliente = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            System.out.println("Connecting to broker: " + broker);
            cliente.connect(connOpts);

            //cliente.subscribe("#", 1);
            //String nome = "orion/temperature";
            cliente.subscribe(topics, qos);
            /**
             * Explicação sobre QOS importante para o tcc
             * https://www.embarcados.com.br/mqtt-protocolos-para-iot/
             */
            System.out.println("Connected");
            while (true) {
                /*se inscrve para receber as mensagens do tópico "#" todos*/

                //informa quem é o método callblack para ficar ouvindo a rede para 
                //saber quando há mensagens do interesse 'tópico'
                System.out.println("Callback on");
                cliente.setCallback(new SimpleCallback());
                
                Publisher.publicar("Teste MQTT");
                try {
                    Thread.sleep(2500);
                    //cliente.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            System.out.println("Disconnected");
//            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("except " + me);
            me.printStackTrace();
        }

    }
    /**
     * Conexão, segurança e qualidade de serviço
     *
     *
     * A conexão do cliente ao broker, seja ele subscritor ou publicador, é
     * originalmente feita via TCP, com opções de login (usuário e senha) e uso
     * de criptografia (SSL/TLS). É possível encontrar também outros meios
     * físicos, com MQTT rodando em links seriais, por exemplo.
     *
     *
     *
     * Todo processo de conexão estabelece também um nível de qualidade de
     * serviço (QoS, de Quality of Service) desejado, indicando como deve ser a
     * relação entre os elementos comunicantes. São previstos três níveis de
     * qualidade de serviço:
     *
     * QoS 0 (at most once): É o que conhecemos como “best effort”, ou melhor
     * esforço. Assemelha-se ao protocolo de transporte UDP, onde não se tem
     * confirmações de entrega de mensagem. Quem envia também não tem a
     * obrigação de manter a mensagem armazenada para futuras retransmissões;
     *
     * QoS 1 (at least once): Neste nível existe a confirmação de entrega de uma
     * mensagem. Atende situações onde quem envia acaba gerando várias mensagens
     * iguais possivelmente por um atraso na chegada de confirmação de
     * recebimento. Neste caso, é garantido que uma delas terá o reconhecimento
     * realizado. Note que existe um armazenamento da mensagem por parte de quem
     * envia até a posterior confirmação;
     *
     * QoS 2 (exactly once): Garante que a mensagem seja entregue exatamente uma
     * vez, com envio de confirmações de recebimento e confirmações de
     * recebimento de confirmações de recebimento (!). Parece confuso, mas não
     * é, apenas existem confirmações nos dois sentidos, para tudo que é
     * trafegado. Enquanto uma mensagem não é confirmada, ela é mantida. É um
     * caso mais próximo do protocolo de transporte TCP.
     *
     *
     * Não existe um QoS melhor ou pior, isto irá depender de cada cenário de
     * aplicação do MQTT, da qualidade do link de comunicação, dos recursos
     * disponíveis no seu sensor, etc. E, é importante ressaltar, cada nível de
     * QoS é negociado entre o cliente e o broker, não entre o publicador e o
     * subscritor. Assim, é possível ter uma publicação em QoS 0 e uma
     * subscrição em QoS 2, para um mesmo tópico.
     *
     *
     * Post da imb sobre o QOS
     * https://www.ibm.com/support/knowledgecenter/pt-br/SSFKSJ_8.0.0/com.ibm.mq.dev.doc/q029090_.htm
     * Um cliente MQTT fornece três qualidades de serviço para entrega de
     * publicações para IBM® MQ e para o cliente MQTT: "no máximo uma vez", "no
     * mínimo uma vez" e "exatamente uma vez". Quando um cliente MQTT enviar uma
     * solicitação ao IBM MQ para criar uma assinatura, a solicitação será
     * enviada com a qualidade de serviço "pelo menos uma vez".
     *
     * A qualidade de serviço de uma publicação é um atributo de MqttMessage.
     * Ela é configurada pelo método MqttMessage.setQos.
     *
     * O método MqttClient.subscribe pode reduzir a qualidade de serviço
     * aplicada às publicações enviadas para um cliente em um tópico. A
     * qualidade de serviço de uma publicação encaminhada para um assinante pode
     * ser diferente da qualidade de serviço da publicação. O menor dos dois
     * valores é usado para encaminhar uma publicação.
     *
     * No máximo uma vez 
     * QoS=0 
     * 
     * A mensagem é entregue no máximo uma vez ou não é
     * entregue de modo algum. Sua entrega na rede não é reconhecida. A mensagem
     * não é armazenada. A mensagem poderá ser perdida se o cliente for
     * desconectado ou se o servidor falhar. QoS=0 é o modo de transferência
     * mais rápido. Ele é, às vezes, chamado de "fire and forget". O Protocolo
     * MQTT não requer que servidores encaminhem publicações no QoS=0 para um
     * cliente. Se o cliente estiver desconectado no momento em que o servidor
     * receber a publicação, a publicação poderá ser descartada, dependendo do
     * servidor. O serviço de telemetria (MQXR) não descarta mensagens enviadas
     * com QoS=0. Elas são armazenadas como mensagens não persistentes e só
     * serão descartadas, se o gerenciador de filas for interrompido. 
     * 
     * Pelo menos uma vez 
     * QoS=1 
     * QoS=1 é o modo de transferência padrão. 
     * 
     * A mensagem é sempre
     * entregue pelo menos uma vez. Se o emissor não receber uma confirmação, a
     * mensagem será enviada novamente com o sinalizador DUP configurado até que
     * uma confirmação seja recebida. Como resultado, o receptor pode receber a
     * mesma mensagem diversas vezes e processá-la diversas vezes. A mensagem
     * deve ser armazenada localmente no emissor e no receptor até ser
     * processada. A mensagem será excluída do receptor após ter processado a
     * mensagem. Se o receptor for um broker, a mensagem será publicada para
     * seus assinantes. Se o receptor for um cliente, a mensagem será entregue
     * para o aplicativo de assinante. Após a mensagem ser excluída, o receptor
     * enviará uma confirmação para o emissor. A mensagem será excluída do
     * emissor após ter recebido uma confirmação do receptor. 
     * 
     * Exatamente uma vez
     * QoS=2 
     * A mensagem é sempre entregue exatamente uma vez. 
     * 
     * A mensagem deve ser armazenada localmente no emissor e no receptor até ser processada.
     * QoS=2 é o mais seguro e o modo de transferência mais lento. Ele levará
     * pelo menos dois pares de transmissões entre o emissor e o receptor antes
     * de a mensagem ser excluída do emissor. A mensagem poderá ser processada
     * no receptor após a primeira transmissão. No primeiro par de transmissões,
     * o emissor transmite a mensagem e recebe a confirmação do receptor de que
     * ele armazenou a mensagem. Se o emissor não receber uma confirmação, a
     * mensagem será enviada novamente com o sinalizador DUP configurado até que
     * uma confirmação seja recebida. No segundo par de transmissões, o emissor
     * informa ao receptor que ele pode concluir o processamento da mensagem,
     * PUBREL. Se o emissor não receber uma confirmação da mensagem PUBREL, a
     * mensagem PUBREL será enviada novamente até que uma confirmação seja
     * recebida. O emissor excluirá a mensagem que salvou ao receber a
     * confirmação para a mensagem PUBREL O receptor poderá processar a mensagem
     * na primeira ou na segunda fase, contanto que não reprocesse a mensagem.
     * Se o receptor for um broker, ele publicará a mensagem para os assinantes.
     * Se o receptor for um cliente, ele entregará a mensagem para o aplicativo
     * de assinante. O receptor envia uma mensagem de conclusão de volta para o
     * emissor que concluiu o processamento da mensagem.
     */
}
